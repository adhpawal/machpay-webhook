package com.lftechnology.machpay.webhook.enums;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by prkandel on 5/1/17.
 */

public enum Role {
    MTO("MTO"),
    SENDER("SENDER"),
    MSB("MSB");

    public static final Map<String, Role> roleIndex = Maps.newHashMapWithExpectedSize(Role.values().length);

    static {
        for (Role role : Role.values()) {
            roleIndex.put(role.value, role);
        }
    }

    public final String value;

    Role(String value) {
        this.value = value;
    }

    public static Role getRole(String value) {
        return roleIndex.get(value);
    }
}
