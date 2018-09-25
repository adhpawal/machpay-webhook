package com.lftechnology.machpay.webhook.service;

import com.lftechnology.machpay.webhook.dto.WebhookRequestDto;
import com.lftechnology.machpay.webhook.dto.WebhookResponseDto;

/**
 * Created by adhpawal on 9/4/17.
 */
public interface WebhookHttpService {

    WebhookResponseDto post(WebhookRequestDto webhookRequestDto);

}
