package com.lftechnology.machpay.webhook.entity;

import com.lftechnology.machpay.common.entity.UniqueEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by adhpawal on 4/30/17.
 */

@Entity
@Table(name = "subscription")
public class Subscription extends UniqueEntity implements Serializable {

    @NotNull
    @Column(name = "company_id")
    private Long companyId;

    @Version
    private Long version;

    @NotNull
    private String endPoint;

    @NotNull
    private String accessKey;

    @NotNull
    @Column(name = "active_from")
    private LocalDateTime activeFrom;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "is_paused")
    private Boolean paused;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "failure_count")
    private Integer failureCount = Integer.MIN_VALUE;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public LocalDateTime getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(LocalDateTime activeFrom) {
        this.activeFrom = activeFrom;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Boolean getPaused() {
        return paused;
    }

    public void setPaused(Boolean paused) {
        this.paused = paused;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }
}
