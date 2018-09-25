package com.lftechnology.machpay.webhook.service.impl;

import com.lftechnology.machpay.common.exception.ObjectNotFoundException;
import com.lftechnology.machpay.webhook.dao.SubscriptionDao;
import com.lftechnology.machpay.webhook.dto.SubscriptionDto;
import com.lftechnology.machpay.webhook.entity.Subscription;
import com.lftechnology.machpay.webhook.service.SubscriptionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class SubscriptionServiceImpl implements SubscriptionService {

    @Inject
    private SubscriptionDao subscriptionDao;

    @Override
    public Subscription save(Long companyId, SubscriptionDto subscriptionDto) {
        Subscription subscription = new Subscription();
        subscription.setCompanyId(companyId);
        subscription.setActiveFrom(LocalDateTime.now());
        subscription.setEndPoint(subscriptionDto.getEndPoint());
        subscription.setAccessKey(subscriptionDto.getSecret());
        subscription.setActive(Boolean.TRUE);
        subscription.setPaused(Boolean.FALSE);
        return subscriptionDao.save(subscription);
    }

    @Override
    public Subscription pause(String subscriptionId, Boolean pause) {
        Subscription subscription = subscriptionDao.findById(UUID.fromString(subscriptionId));
        if (subscription == null) {
            throw new ObjectNotFoundException("No row with the given identifier.");
        }
        subscription.setPaused(pause);
        return subscriptionDao.update(subscription);
    }

    @Override
    public List<Subscription> findByFilter() {
        return subscriptionDao.findByFilter();
    }

    @Override
    public Subscription findByCompanyIdAndSubscriptionId(Long companyId, String subscriptionId) {
        Optional<Subscription> subscription = subscriptionDao.findBySubscriptionIdAndCompanyId(subscriptionId, companyId);
        if (!subscription.isPresent()) {
            throw new ObjectNotFoundException("No row with the given identifier.");
        }
        return subscription.get();

    }

    @Override
    public List<Subscription> findByCompanyId(Long companyId) {
        return subscriptionDao.findByCompanyId(companyId);
    }

    @Override
    public List<Subscription> findActiveSubscriptionByCompanyId(Long companyId) {
        return subscriptionDao.findActiveSubscriptionByCompanyId(companyId);
    }

    @Override
    public void remove(String subscriptionId) {
        Subscription subscription = subscriptionDao.findById(UUID.fromString(subscriptionId));
        if (subscription == null) {
            throw new ObjectNotFoundException("No row with the given identifier.");
        }
        subscription.setActive(Boolean.FALSE);
        subscription.setExpiredAt(LocalDateTime.now());
        subscriptionDao.update(subscription);
    }
}






