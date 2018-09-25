package com.lftechnology.machpay.webhook.service;

import com.lftechnology.machpay.webhook.dto.WebhookRequestDto;
import com.lftechnology.machpay.webhook.entity.Event;
import com.lftechnology.machpay.webhook.entity.Subscription;

/**
 * Created by adhpawal on 9/3/17.
 */
public interface WebhookRequestService {

    void triggerWebhook(Event event, Subscription subscription);

    void triggerScheduledWebhook(WebhookRequestDto webhookRequestDto);
}
