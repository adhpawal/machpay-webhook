package com.lftechnology.machpay.webhook.dao.impl;

import com.lftechnology.machpay.common.dao.impl.BaseDao;
import com.lftechnology.machpay.webhook.dao.EventDao;
import com.lftechnology.machpay.webhook.entity.Event;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by adhpawal on 9/3/17.
 */
@Stateless
public class EventDaoImpl extends BaseDao<Event, UUID> implements EventDao {

    public EventDaoImpl() {
        super(Event.class);
    }

    @Override
    public List<Event> findAllByCompanyId(Long companyId) {
        TypedQuery<Event> query = em.createQuery("select e from Event e where e.mtoId = :companyId", Event.class);
        query.setParameter("companyId", companyId);
        return query.getResultList();
    }

    @Override
    public List<Event> findAllPendingEvent() {
        TypedQuery<Event> query = em.createQuery("select e from Event e where e.isExpired = false", Event.class);
        return query.getResultList();
    }

    @Override
    public Optional<Event> findByIdAndCompnayId(UUID eventId, Long companyId) {
        TypedQuery<Event> query = em.createQuery("select e from Event e where e.mtoId = :companyId and id= :id", Event.class);
        query.setParameter("companyId", companyId);
        query.setParameter("id", eventId);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Event> findLatestEventByPersistedObjectId(String persistedObjectId, Long compnayId) {
        TypedQuery<Event> query = em.createQuery("select e from Event e where e.mtoId = :companyId and persistedObjectId= :persistedObjectId order by createdAt DESC ", Event.class);
        query.setParameter("persistedObjectId", persistedObjectId);
        query.setParameter("companyId", compnayId);
        try {
            return Optional.of(query.setMaxResults(1).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
