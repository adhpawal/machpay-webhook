package com.lftechnology.machpay.webhook.startup;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class StartupAction {

    //FIXME : Implement Methods to Rerun the Failed Webhooks
    @PostConstruct
    public void init() {

    }
}
