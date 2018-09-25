package com.lftechnology.machpay.webhook.dao;

import com.lftechnology.machpay.common.dao.CrudDao;
import com.lftechnology.machpay.webhook.entity.Subscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionDao extends CrudDao<Subscription, UUID> {

    List<Subscription> findByFilter();

    Optional<Subscription> findBySubscriptionIdAndCompanyId(String subscriptionId, Long companyId);

    List<Subscription> findByCompanyId(Long companyId);

    List<Subscription> findActiveSubscriptionByCompanyId(Long companyId);


}




