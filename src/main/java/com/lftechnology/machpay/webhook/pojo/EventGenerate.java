package com.lftechnology.machpay.webhook.pojo;

import java.util.UUID;

public class EventGenerate {

    UUID eventId;

    public EventGenerate(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }
}
