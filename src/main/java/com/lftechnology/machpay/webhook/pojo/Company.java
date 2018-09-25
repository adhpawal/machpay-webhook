package com.lftechnology.machpay.webhook.pojo;

/**
 * Created by adhpawal on 9/4/17.
 */

public class Company {
    private Long companyId;

    public Company() {
    }

    public Company(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
