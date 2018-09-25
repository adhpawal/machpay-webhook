package com.lftechnology.machpay.webhook.dao.impl;

import com.lftechnology.machpay.common.dao.impl.BaseDao;
import com.lftechnology.machpay.webhook.dao.SubscriptionDao;
import com.lftechnology.machpay.webhook.entity.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class SubscriptionDaoImpl extends BaseDao<Subscription, UUID> implements SubscriptionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionDaoImpl.class);

    public SubscriptionDaoImpl() {
        super(Subscription.class);
    }

    @Override
    public List<Subscription> findByFilter() {
        return null;
    }

    @Override
    public Optional<Subscription> findBySubscriptionIdAndCompanyId(String subscriptionId, Long companyId) {
        TypedQuery<Subscription> query = em.createQuery("Select s from Subscription s where s.companyId =:companyId and s.id = :subscriptionId", Subscription.class);
        query.setParameter("companyId", companyId);
        query.setParameter("subscriptionId", UUID.fromString(subscriptionId));
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Subscription> findByCompanyId(Long companyId) {
        TypedQuery<Subscription> query = em.createQuery("Select s from Subscription s where s.companyId =:companyId", Subscription.class);
        query.setParameter("companyId", companyId);
        return query.getResultList();
    }

    @Override
    public List<Subscription> findActiveSubscriptionByCompanyId(Long companyId) {
        TypedQuery<Subscription> query = em.createQuery("Select s from Subscription s where s.companyId =:companyId and s.active=true and s.paused=false", Subscription.class);
        query.setParameter("companyId", companyId);
        return query.getResultList();
    }
}
