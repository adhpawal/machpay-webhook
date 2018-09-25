package com.lftechnology.machpay.webhook.dao.impl;

import com.lftechnology.machpay.common.dao.impl.BaseDao;
import com.lftechnology.machpay.webhook.dao.WebhookRequestDao;
import com.lftechnology.machpay.webhook.entity.WebhookRequest;

import javax.ejb.Stateless;
import java.util.UUID;

/**
 * Created by adhpawal on 9/3/17.
 */
@Stateless
public class WebhookRequestDaoImpl extends BaseDao<WebhookRequest, UUID> implements WebhookRequestDao {
    public WebhookRequestDaoImpl() {
        super(WebhookRequest.class);
    }

}
