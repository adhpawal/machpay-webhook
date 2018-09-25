package com.lftechnology.machpay.webhook.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * Created by adhpawal on 9/4/17.
 */
public class WebhookRequestDto implements Serializable {

    private static final long serialVersionUID = 6189252480182330571L;

    String url;

    UUID eventId;
    UUID subscriptionId;

    Map<String, String> requestBody;

    Long companyId;

    String eventName;
    String signature;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Map<String, String> requestBody) {
        this.requestBody = requestBody;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(UUID subscriptionId) {
        this.subscriptionId = subscriptionId;
    }


    @Override
    public String toString() {
        return "WebhookRequestDto{" +
                "url='" + url + '\'' +
                ", eventId=" + eventId +
                ", subscriptionId=" + subscriptionId +
                ", requestBody=" + requestBody +
                ", companyId=" + companyId +
                ", eventName='" + eventName + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
