package com.lftechnology.machpay.webhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Inject Log.
 *
 * @author Achyut Pokhrel <achyutpokhrel@lftechnology.com>
 */
public class LoggerProducer {

    @Produces
    public Logger loggerProducer(InjectionPoint ip) {
        return LoggerFactory.getLogger(ip.getMember().getDeclaringClass().getName());
    }
}
