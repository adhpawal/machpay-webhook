package com.lftechnology.machpay.webhook.dao;

import com.lftechnology.machpay.common.dao.CrudDao;
import com.lftechnology.machpay.webhook.entity.Event;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by adhpawal on 9/3/17.
 */
public interface EventDao extends CrudDao<Event, UUID> {

    List<Event> findAllByCompanyId(Long companyId);

    Optional<Event> findByIdAndCompnayId(UUID eventId, Long compnayId);

    Optional<Event> findLatestEventByPersistedObjectId(String persistedObjectId, Long compnayId);

    List<Event> findPendingEventByPersistedObjectId(String persistedObjectId);

    List<Event> findAllPendingEvent();

}
