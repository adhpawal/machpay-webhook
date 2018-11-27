package com.lftechnology.machpay.webhook.job;

import com.lftechnology.machpay.webhook.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@Startup
public class PendingEventScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PendingEventScheduler.class);

    @Inject
    private EventService eventService;

    @Schedule(persistent = false, second = "*", minute = "*/30", hour = "*")
    public void execute() {
        LOGGER.info("Start Payment Processor Authentication Refresh Job");
        eventService.rerunPendingEvents();
        LOGGER.info("Completed Payment Processor Refresh Job");
    }
}
