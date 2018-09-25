package com.lftechnology.machpay.webhook;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Achyut Pokhrel <achyutpokhrel@lftechnology.com>
 */
public class Resource {

    @Produces
    @PersistenceContext
    private EntityManager em;

}
