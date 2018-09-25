package com.lftechnology.machpay.webhook.service.impl;

import com.lftechnology.machpay.common.exception.BadRequestException;
import com.lftechnology.machpay.webhook.dao.EventDao;
import com.lftechnology.machpay.webhook.entity.Event;
import com.lftechnology.machpay.webhook.service.EventService;
import com.lftechnology.machpay.webhook.service.IncomingEventService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by adhpawal on 9/4/17.
 */
@Stateless
public class EventServiceImpl implements EventService {

    @Inject
    private EventDao eventDao;

    @Inject
    private IncomingEventService incomingEventService;

    @Override
    public void save(Event event) {
        eventDao.save(event);
    }

    @Override
    public List<Event> findAllByCompany(Long companyId) {
        return eventDao.findAllByCompanyId(companyId);
    }

    @Override
    public Event findByIdAndCompany(UUID eventId, Long companyId) {
        Optional<Event> event = eventDao.findByIdAndCompnayId(eventId, companyId);
        if (!event.isPresent()) {
            throw new BadRequestException();
        }
        return event.get();
    }

    @Override
    public Event getLatestEvent(String persistedObjectId, Long companyId){
        Optional<Event> event = eventDao.findLatestEventByPersistedObjectId(persistedObjectId, companyId);
        if(event.isPresent()){
            return event.get();
        }
        return null;
    }

    @Override
    public void rerunPendingEvents(){
        List<Event> events = eventDao.findAllPendingEvent();
        events.forEach(event->{
            incomingEventService.triggerSubscription(event, event.getMtoId());
        });
    }
}
