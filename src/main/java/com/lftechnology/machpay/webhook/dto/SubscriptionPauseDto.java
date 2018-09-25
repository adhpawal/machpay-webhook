package com.lftechnology.machpay.webhook.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by adhpawal on 5/2/17.
 */
public class SubscriptionPauseDto {

    @NotNull
    private Boolean pause;

    public Boolean isPause() {
        return pause;
    }

    public void setPause(Boolean pause) {
        this.pause = pause;
    }
}
