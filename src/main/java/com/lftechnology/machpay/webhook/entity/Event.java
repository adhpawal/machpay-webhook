package com.lftechnology.machpay.webhook.entity;

import com.lftechnology.machpay.common.entity.UniqueEntity;
import com.lftechnology.machpay.common.enums.SubscriptionEventName;
import com.lftechnology.machpay.common.enums.SubscriptionEventType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by adhpawal on 4/30/17.
 */
@Entity
@Table(name = "event")
public class Event extends UniqueEntity implements Serializable {

    @NotNull
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    SubscriptionEventName name;

    @NotNull
    @Column(name = "logged_at")
    private LocalDateTime loggedAt;

    @NotNull
    @Column(name = "persisted_object_id")
    private String persistedObjectId;

    @Column(name = "sender_id")
    private String senderId;

    @NotNull
    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private SubscriptionEventType eventType;

    @Column(name = "msb_id")
    private Long msbId;

    @Column(name = "mto_id")
    private Long mtoId;

    @Column(name = "is_expired")
    private Boolean isExpired = Boolean.FALSE;

    public SubscriptionEventName getName() {
        return name;
    }

    public void setName(SubscriptionEventName name) {
        this.name = name;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }

    public String getPersistedObjectId() {
        return persistedObjectId;
    }

    public void setPersistedObjectId(String persistedObjectId) {
        this.persistedObjectId = persistedObjectId;
    }

    public SubscriptionEventType getEventType() {
        return eventType;
    }

    public void setEventType(SubscriptionEventType eventType) {
        this.eventType = eventType;
    }

    public Long getMsbId() {
        return msbId;
    }

    public void setMsbId(Long msbId) {
        this.msbId = msbId;
    }

    public Long getMtoId() {
        return mtoId;
    }

    public void setMtoId(Long mtoId) {
        this.mtoId = mtoId;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}


