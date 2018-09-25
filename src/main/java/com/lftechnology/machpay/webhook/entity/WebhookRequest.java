package com.lftechnology.machpay.webhook.entity;

import com.lftechnology.machpay.common.entity.UniqueEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by adhpawal on 5/1/17.
 */
@Entity
@Table(name = "webhook_request")
public class WebhookRequest extends UniqueEntity implements Serializable {

    @JoinColumn(name = "event_id")
    private Event event;

    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Column(name="is_success")
    private Boolean isSuccess;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

}
