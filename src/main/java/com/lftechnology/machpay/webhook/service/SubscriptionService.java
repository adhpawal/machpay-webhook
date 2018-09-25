package com.lftechnology.machpay.webhook.service;


import com.lftechnology.machpay.webhook.dto.SubscriptionDto;
import com.lftechnology.machpay.webhook.entity.Subscription;

import java.util.List;


public interface SubscriptionService {

    Subscription save(Long companyId, SubscriptionDto subscription);

    List<Subscription> findByFilter();

    Subscription findByCompanyIdAndSubscriptionId(Long companyId, String subscriptionId);

    List<Subscription> findByCompanyId(Long companyId);

    List<Subscription> findActiveSubscriptionByCompanyId(Long companyId);

    Subscription pause(String subscriptionId, Boolean pause);

    void remove(String subscriptionId);
}