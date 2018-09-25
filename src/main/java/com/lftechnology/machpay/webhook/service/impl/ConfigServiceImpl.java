package com.lftechnology.machpay.webhook.service.impl;

import com.lftechnology.machpay.common.Property;
import com.lftechnology.machpay.webhook.service.ConfigService;

import javax.inject.Inject;

/**
 * Created by adhpawal on 4/11/17.
 */
public class ConfigServiceImpl implements ConfigService {

    @Inject
    @Property("tenant.identifier.url")
    private String tenantIdentifierUrl;

    @Inject
    @Property("webhook.delay.minutes")
    private Long webhookDelayMinutes;

    @Inject
    @Property("redis.channel.name")
    private String channelName;

    @Inject
    @Property("webhook.failure.threshold")
    private Long failureThreshold;

    @Override
    public String getTenantIdentifierUrl() {
        return this.tenantIdentifierUrl;
    }

    @Override
    public Long getWebhookDelayInMinutes() {
        return this.webhookDelayMinutes;
    }

    @Override
    public Long getWebhookFailureThreshold() {
        return this.failureThreshold;
    }

    @Override
    public String getChannelName() {
        return this.channelName;
    }
}
