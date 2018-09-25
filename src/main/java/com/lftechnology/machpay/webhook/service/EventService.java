package com.lftechnology.machpay.webhook.service;

import com.lftechnology.machpay.webhook.entity.Event;

import java.util.List;
import java.util.UUID;

/**
 * Created by adhpawal on 9/4/17.
 */
public interface EventService {

    void save(Event event);

    List<Event> findAllByCompany(Long companyId);

    Event findByIdAndCompany(UUID eventId, Long companyId);

    Event getLatestEvent(String persistedObjectId, Long companyId);

    void rerunPendingEvents();
}
