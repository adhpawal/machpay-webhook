package com.lftechnology.machpay.webhook.service.impl;

import com.lftechnology.machpay.common.dto.event.SubscriptionEvent;
import com.lftechnology.machpay.common.enums.SubscriptionEventName;
import com.lftechnology.machpay.common.enums.SubscriptionEventType;
import com.lftechnology.machpay.webhook.entity.Event;
import com.lftechnology.machpay.webhook.entity.Subscription;
import com.lftechnology.machpay.webhook.observer.EntityEventObserver;
import com.lftechnology.machpay.webhook.pojo.EventGenerate;
import com.lftechnology.machpay.webhook.service.EventService;
import com.lftechnology.machpay.webhook.service.IncomingEventService;
import com.lftechnology.machpay.webhook.service.SubscriptionService;
import com.lftechnology.machpay.webhook.service.WebhookRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by adhpawal on 9/13/17.
 */

@Stateless
public class IncomingEventServiceImpl implements IncomingEventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingEventServiceImpl.class.getName());

    @Inject
    private EventService eventService;

    @Inject
    private WebhookRequestService webhookRequestService;

    @Inject
    private SubscriptionService subscriptionService;

    @Inject
    private javax.enterprise.event.Event<EventGenerate> observerEvent;

    @Override
    public void registerEvent(SubscriptionEvent eventDto) {
        Event event = new Event();
        event.setName(SubscriptionEventName.get(eventDto.getEventName()));
        event.setPersistedObjectId(eventDto.getPersistedObjectId());
        event.setEventType(SubscriptionEventType.valueOf(eventDto.getType()));
        event.setMsbId(eventDto.getMsbId());
        event.setMtoId(eventDto.getMtoId());
        event.setLoggedAt(LocalDateTime.now());
        event.setExpired(Boolean.FALSE);
        event.setSenderId(eventDto.getSenderId());
        Event eventExisting = eventService.getLatestEvent(event.getPersistedObjectId(),event.getMtoId());
        if(eventExisting == null || eventExisting.getName()!=event.getName()){
            eventService.save(event);
            observerEvent.fire(new EventGenerate(event.getId()));
        }
    }

    @Override
    public void triggerSubscription(Event event, Long mtoId) {
        LOGGER.info("#triggerSubscription Fetch Active Subscriptions for MTO : {}", mtoId);
        List<Subscription> subscriptions = subscriptionService.findActiveSubscriptionByCompanyId(mtoId);
        LOGGER.info("#triggerSubscription MTO : {} Total Active Subscriptions for MTO : {}", mtoId, subscriptions.size());
        subscriptions.forEach(subscription -> {
                    LOGGER.info("#triggerSubscription Initiate Webhook for Subscription URL {}", subscription.getEndPoint());
                    webhookRequestService.triggerWebhook(event, subscription);
                }
        );
    }
}
