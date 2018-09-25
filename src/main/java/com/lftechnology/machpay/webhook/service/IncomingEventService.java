package com.lftechnology.machpay.webhook.service;

import com.lftechnology.machpay.common.dto.event.SubscriptionEvent;
import com.lftechnology.machpay.webhook.entity.Event;

/**
 * Created by adhpawal on 9/13/17.
 */
public interface IncomingEventService {

    void registerEvent(SubscriptionEvent eventDto);

    void triggerSubscription(Event event, Long mtoId);
}
