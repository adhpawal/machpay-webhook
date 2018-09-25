package com.lftechnology.machpay.webhook.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lftechnology.machpay.common.enums.SubscriptionEventType;
import com.lftechnology.machpay.common.exception.BadRequestException;
import com.lftechnology.machpay.webhook.constants.CommonConstant;
import com.lftechnology.machpay.webhook.dao.EventDao;
import com.lftechnology.machpay.webhook.dao.SubscriptionDao;
import com.lftechnology.machpay.webhook.dao.WebhookRequestDao;
import com.lftechnology.machpay.webhook.dto.WebhookRequestDto;
import com.lftechnology.machpay.webhook.dto.WebhookResponseDto;
import com.lftechnology.machpay.webhook.entity.Event;
import com.lftechnology.machpay.webhook.entity.Subscription;
import com.lftechnology.machpay.webhook.entity.WebhookRequest;
import com.lftechnology.machpay.webhook.service.ConfigService;
import com.lftechnology.machpay.webhook.service.WebhookHttpService;
import com.lftechnology.machpay.webhook.service.WebhookRequestService;
import com.lftechnology.machpay.webhook.util.EncryptUtils;
import job.WebhookTriggerJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adhpawal on 9/3/17.
 */
@Stateless
public class WebhookRequestServiceImpl implements WebhookRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookRequestServiceImpl.class.getName());

    @Inject
    private EventDao eventDao;

    @Inject
    private SubscriptionDao subscriptionDao;

    @Inject
    private WebhookRequestDao webRequestDao;

    @Inject
    private WebhookHttpService webhookHttpService;

    @Inject
    private WebhookTriggerJob webhookTriggerJob;

    @Inject
    private ConfigService configService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void triggerWebhook(Event event, Subscription subscription) {
        LOGGER.info("WebhookRequestServiceImpl#triggerWebhook Event :: {} Sub : {}", event.getId(), subscription.getId());
        WebhookRequest webhookRequest = new WebhookRequest();
        WebhookRequestDto webhookRequestDto = new WebhookRequestDto();
        webhookRequestDto.setCompanyId(subscription.getCompanyId());
        webhookRequestDto.setEventName(event.getName().getName());
        webhookRequest.setSubscription(subscription);
        webhookRequest.setEvent(event);
        webhookRequest.setSuccess(Boolean.FALSE);
        webhookRequest = webRequestDao.save(webhookRequest);
        webhookRequestDto.setRequestBody(prepareRequestBody(event, subscription, webhookRequest));
        try {
            webhookRequestDto.setSignature(EncryptUtils.encode(subscription.getAccessKey(),
                    EncryptUtils.getJSONStringFromMap(webhookRequestDto.getRequestBody())));
        } catch (Exception e) {
            LOGGER.error("WebhookRequestServiceImpl#triggerWebhook Error {}", e);
            throw new BadRequestException();
        }
        webhookRequestDto.setUrl(subscription.getEndPoint());
        WebhookResponseDto webhookResponseDto = webhookHttpService.post(webhookRequestDto);
        LOGGER.info("#triggerSubscription Subscription URL {}, Request : {}, Response : {}", subscription.getEndPoint(), webhookRequestDto.toString(), webhookResponseDto.toString());
        webhookRequest.setResponseStatus(Integer.valueOf(webhookResponseDto.getHttpCode()));
        webhookRequest.setSuccess(Integer.valueOf(webhookResponseDto.getHttpCode()).equals(CommonConstant.HTTP_OK));
        webhookRequest.setUpdatedAt(LocalDateTime.now());
        try {
            webhookRequest.setRequestBody(new ObjectMapper().writeValueAsString(webhookRequestDto.getRequestBody()));
        } catch (JsonProcessingException e) {
            LOGGER.error("WebhookRequestServiceImpl#triggerWebhook Error While Preparing Request JSON. Message {}", e);
        }
        webRequestDao.update(webhookRequest);
        if (!webhookRequest.getSuccess()) {
            LOGGER.info("WebhookRequestServiceImpl#triggerWebhook. Unsuccessful attempt on Subscription : {}, Error Code : {}", subscription.getId(), webhookRequest.getResponseStatus());
            webhookRequestDto.setEventId(event.getId());
            webhookRequestDto.setSubscriptionId(subscription.getId());
            subscription.setFailureCount(subscription.getFailureCount() + 1);
            if (subscription.getFailureCount() > configService.getWebhookFailureThreshold()) {
                LOGGER.info("#triggerWebhook Threshold Exceeded for Subscription : {}", subscription.getId());
                subscription.setFailureCount(0);
                subscription.setActive(Boolean.FALSE);
            } else {
                if(Duration.between(event.getCreatedAt(), LocalDateTime.now()).toHours() > 72){
                    event.setExpired(Boolean.TRUE);
                }else{
                    webhookTriggerJob.initialize(webhookRequestDto);
                }
            }
        }else{
            event.setExpired(Boolean.TRUE);
        }
        eventDao.update(event);
        subscriptionDao.update(subscription);
    }

    @Override
    public void triggerScheduledWebhook(WebhookRequestDto webhookRequestDto) {
        LOGGER.info("#triggerScheduledWebhook Scheduled Webhook Trigger Started for Request : {}", webhookRequestDto.toString());
        Event event = eventDao.findById(webhookRequestDto.getEventId());
        Subscription subscription = subscriptionDao.findById(webhookRequestDto.getSubscriptionId());
        LOGGER.info("#triggerScheduledWebhook Scheduled Webhook Trigger Started for Request : {}, {}",event.getId(), event.getExpired());
        LOGGER.info("#triggerScheduledWebhook Scheduled Webhook Trigger Started for Request : {}, {}",subscription.getId(), subscription.getPaused());
        if (!subscription.getPaused() && !event.getExpired()) {
            triggerWebhook(event, subscription);
        }
    }

    /**
     * <code>
     * <p>
     * {
     * "id": "82646f2c-447a-4214-a6ad-7d43e87d7fc4",
     * "resource_id": "dab8da2c-61ea-42cf-a7c5-223967bebb81",
     * "sender_id": "ee3aa0ac-8d18-4a7b-adaf-c5cc8a8db62f",
     * "event_name": "transaction_completed",
     * "subscription_id": "caec6725-bf0a-4ca4-9f8c-153f4da87259",
     * "event_id": "cf8ae9f3-dc8b-4317-8307-a0d71294e9a7",
     * "timestamp": "2017-07-02T22:88:12.120Z"
     * }
     * <p>
     * </code>
     *
     * @param event
     * @param subscription
     * @return Map
     */
    private Map<String, String> prepareRequestBody(Event event, Subscription subscription,
                                                   WebhookRequest webhookRequest) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("id", String.valueOf(webhookRequest.getId()));
        requestBody.put("resource_id", event.getPersistedObjectId());
        if((event.getSenderId()!=null && !event.getSenderId().equalsIgnoreCase("null")) && !SubscriptionEventType.SENDER.equals(event.getEventType())){
            requestBody.put("sender_id", event.getSenderId());
        }
        requestBody.put("event_name", event.getName().getName());
        requestBody.put("subscription_id", String.valueOf(subscription.getId()));
        requestBody.put("event_id", String.valueOf(event.getId()));
        requestBody.put("timestamp", LocalDateTime.now().toString());
        return requestBody;
    }
}
