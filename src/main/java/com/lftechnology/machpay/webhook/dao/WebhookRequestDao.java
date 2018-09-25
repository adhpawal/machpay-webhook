package com.lftechnology.machpay.webhook.dao;

import com.lftechnology.machpay.common.dao.CrudDao;
import com.lftechnology.machpay.webhook.entity.WebhookRequest;

import java.util.UUID;

/**
 * Created by adhpawal on 9/3/17.
 */
public interface WebhookRequestDao extends CrudDao<WebhookRequest, UUID> {
}
