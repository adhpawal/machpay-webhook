package com.lftechnology.machpay.webhook.service;

/**
 * @author adhpawal
 */
public interface ConfigService {

    String getTenantIdentifierUrl();

    Long getWebhookDelayInMinutes();

    Long getWebhookFailureThreshold();

    String getChannelName();
}
