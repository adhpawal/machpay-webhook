package com.lftechnology.machpay.webhook.observer;

import com.lftechnology.machpay.common.annotation.Logged;
import com.lftechnology.machpay.webhook.dao.EventDao;
import com.lftechnology.machpay.webhook.entity.Event;
import com.lftechnology.machpay.webhook.pojo.EventGenerate;
import com.lftechnology.machpay.webhook.service.IncomingEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

@Stateless
@Logged
public class EntityEventObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityEventObserver.class);

    @Inject
    private IncomingEventService incomingEventService;

    @Inject
    private EventDao eventDao;

    @Asynchronous
    public void fire(@Observes(during = TransactionPhase.AFTER_SUCCESS) EventGenerate eventGenerate) {
        LOGGER.info("EntityEventObserver#fire Event Generate Trigger for : {}", eventGenerate.getEventId());
        try {
            Event event = eventDao.findById(eventGenerate.getEventId());
            LOGGER.info("EntityEventObserver#fire Event Generate Trigger for : {}", event.getId());
            incomingEventService.triggerSubscription(event,event.getMtoId());
        } catch (Exception e) {
            LOGGER.error("Error while mapping event : {}", e);
        }
    }
}
