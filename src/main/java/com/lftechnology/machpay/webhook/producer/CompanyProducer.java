package com.lftechnology.machpay.webhook.producer;

import com.lftechnology.machpay.webhook.annotation.AuthenticatedCompany;
import com.lftechnology.machpay.webhook.pojo.Company;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

/**
 * Created by prkandel on 4/25/17.
 */

@RequestScoped
public class CompanyProducer {

    @Produces
    @RequestScoped
    @AuthenticatedCompany
    private Company company;

    public void handleCompanyAuthenticationEvent(@Observes @AuthenticatedCompany Company company) {
        this.company = company;
    }
}
