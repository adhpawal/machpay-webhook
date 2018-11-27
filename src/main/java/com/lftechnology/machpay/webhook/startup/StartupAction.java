package com.lftechnology.machpay.webhook.startup;

import com.lftechnology.machpay.webhook.service.EventService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class StartupAction {

    @Inject
    private EventService eventService;

    //FIXME : Implement Methods to Rerun the Failed Webhooks
    @PostConstruct
    public void init() {
        System.out.println("Starting Startup Action");
        eventService.rerunPendingEvents();
    }
}
