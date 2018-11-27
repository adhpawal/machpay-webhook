package com.lftechnology.machpay.webhook.service.impl;

import com.lftechnology.machpay.common.dto.event.SubscriptionEvent;
import com.lftechnology.machpay.common.enums.SubscriptionEventName;
import com.lftechnology.machpay.common.enums.SubscriptionEventType;
import com.lftechnology.machpay.webhook.entity.Event;
import com.lftechnology.machpay.webhook.entity.Subscription;
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
import java.util.Arrays;
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
    @Asynchronous
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
            if(eventExisting!=null && !isValidEventOrdering(event, eventExisting)){
                LOGGER.warn("Invalid Ordering of Events");
                return;
            }
            List<Event> existingRunningEvent = eventService.findAllByPersistedObjectId(event.getPersistedObjectId());
            existingRunningEvent.forEach(it->{
                if(it.getEventType() == SubscriptionEventType.TRANSACTION  && !distinguishTransactionEvent(event.getName(), it.getName())){
                    return;
                }
                it.setExpired(Boolean.TRUE);
                eventService.update(it);
            });
            eventService.save(event);
            observerEvent.fire(new EventGenerate(event.getId()));
        }
    }

    @Override
    public void triggerSubscription(Event event, Long mtoId) {
        LOGGER.info("#triggerSubscription Fetch Active Subscriptions for MTO : {}", mtoId);
        if(!event.getExpired()){
            List<Subscription> subscriptions = subscriptionService.findActiveSubscriptionByCompanyId(mtoId);
            LOGGER.info("#triggerSubscription MTO : {} Total Active Subscriptions for MTO : {}", mtoId, subscriptions.size());
            subscriptions.forEach(subscription -> {
                        LOGGER.info("#triggerSubscription Initiate Webhook for Subscription URL {}", subscription.getEndPoint());
                        webhookRequestService.triggerWebhook(event, subscription);
                    }
            );
        }
    }

    private Boolean isValidEventOrdering(Event event, Event latestEvent){
        if(event.getEventType() == SubscriptionEventType.TRANSACTION){
            return Boolean.TRUE;
        }else if (event.getEventType() == SubscriptionEventType.SENDER){
            return isSenderStatusChangeAble(latestEvent.getName(),event.getName());
        }
        return true;
    }

    private Boolean isTransactionStatusChangeable(SubscriptionEventName existingEvent, SubscriptionEventName newEvent) {
        Boolean isChangeAble = Boolean.TRUE;
        switch (existingEvent) {
            case TRANSACTION_CREATED:
                isChangeAble = (newEvent == SubscriptionEventName.TRANSACTION_CANCELED) || (newEvent==SubscriptionEventName.TRANSACTION_COMPLETED) || (newEvent == SubscriptionEventName.TRANSACTION_FAILED);
                break;
            case TRANSACTION_PROCESSED:
                isChangeAble = (newEvent == SubscriptionEventName.TRANSACTION_FAILED) || (newEvent==SubscriptionEventName.TRANSACTION_RETURNED);
                break;
            case TRANSACTION_COMPLETED:
                isChangeAble = (newEvent == SubscriptionEventName.TRANSACTION_FAILED) || (newEvent==SubscriptionEventName.TRANSACTION_RETURNED);
                break;
        }
        return isChangeAble;
    }

    private Boolean isSenderStatusChangeAble(SubscriptionEventName existingEvent, SubscriptionEventName newEvent) {
        Boolean isChangeAble = Boolean.TRUE;
        switch (existingEvent) {
            case SENDER_CREATED:
                isChangeAble = Arrays.asList(SubscriptionEventName.SENDER_DOCUMENTATION_NEEDED, SubscriptionEventName.SENDER_REVERIFICATION_NEEDED, SubscriptionEventName.SENDER_DOCUMENTATION_FAILED, SubscriptionEventName.SENDER_DOCUMENTATION_VERIFIED, SubscriptionEventName.SENDER_SUSPENDED, SubscriptionEventName.SENDER_VERIFIED).contains(newEvent);
                break;
            case SENDER_REVERIFICATION_NEEDED:
                isChangeAble = Arrays.asList(SubscriptionEventName.SENDER_DOCUMENTATION_NEEDED, SubscriptionEventName.SENDER_DOCUMENTATION_VERIFIED, SubscriptionEventName.SENDER_DOCUMENTATION_FAILED, SubscriptionEventName.SENDER_SUSPENDED, SubscriptionEventName.SENDER_VERIFIED).contains(newEvent);
                break;
            case SENDER_DOCUMENTATION_NEEDED:
                isChangeAble = Arrays.asList(SubscriptionEventName.SENDER_DOCUMENTATION_FAILED,SubscriptionEventName.SENDER_DOCUMENTATION_VERIFIED, SubscriptionEventName.SENDER_VERIFIED, SubscriptionEventName.SENDER_SUSPENDED, SubscriptionEventName.SENDER_REVERIFICATION_NEEDED).contains(newEvent);
                break;
            case SENDER_DOCUMENTATION_FAILED:
                isChangeAble = Arrays.asList(SubscriptionEventName.SENDER_REVERIFICATION_NEEDED, SubscriptionEventName.SENDER_DOCUMENTATION_VERIFIED, SubscriptionEventName.SENDER_VERIFIED, SubscriptionEventName.SENDER_SUSPENDED, SubscriptionEventName.SENDER_REVERIFICATION_NEEDED).contains(newEvent);
                break;
            case SENDER_VERIFIED:
                isChangeAble = Arrays.asList(SubscriptionEventName.SENDER_SUSPENDED).contains(newEvent);
                break;
            case SENDER_SUSPENDED:
                isChangeAble = Arrays.asList(SubscriptionEventName.SENDER_VERIFIED, SubscriptionEventName.SENDER_REVERIFICATION_NEEDED, SubscriptionEventName.SENDER_DOCUMENTATION_NEEDED).contains(newEvent);
                break;
        }
        return isChangeAble;
    }

    private Boolean distinguishTransactionEvent(SubscriptionEventName newEventName, SubscriptionEventName existingEventname){
        Boolean allowChange =false;
        if((newEventName.getName().startsWith("transaction_delivery") || newEventName.getName().equalsIgnoreCase("transaction_delivered")) && (existingEventname.getName().startsWith("transaction_delivery") || existingEventname.getName().equalsIgnoreCase("transaction_delivered"))){
            allowChange = true;
        }else if(!newEventName.getName().startsWith("transaction_delivery") && !existingEventname.getName().equalsIgnoreCase("transaction_delivery")) {
            allowChange = true;
        }
        return allowChange;
    }
}
