package com.lftechnology.machpay.webhook.service.impl;

import com.google.gson.JsonObject;
import com.lftechnology.machpay.webhook.constants.CommonConstant;
import com.lftechnology.machpay.webhook.dto.WebhookRequestDto;
import com.lftechnology.machpay.webhook.dto.WebhookResponseDto;
import com.lftechnology.machpay.webhook.service.WebhookHttpService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by adhpawal on 9/4/17.
 */
public class WebhookHttpServiceImpl implements WebhookHttpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookHttpServiceImpl.class.getName());

    @Override
    public WebhookResponseDto post(WebhookRequestDto webhookRequestDto) {
        //FIXME : Move this settings on Server Configuration.
        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1");
        Client client = ClientBuilder.newClient();
        client.property("jersey.config.client.connectTimeout", 100);
        client.property("jersey.config.client.readTimeout", 10);
        WebhookResponseDto webhookResponseDto = new WebhookResponseDto();
        try {
            WebTarget target = client.target(webhookRequestDto.getUrl());
            Response response = target.request().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .header(CommonConstant.WEBHOOK_HEADER_SIGNATURE, webhookRequestDto.getSignature())
                    .header(CommonConstant.WEBHOOK_HEADER_TOPIC, webhookRequestDto.getEventName())
                    .post(Entity.json(getJSONStringFromMap(webhookRequestDto.getRequestBody())));

            webhookResponseDto.setHttpCode(response.getStatus());
            webhookResponseDto.setResponseBody(response.readEntity(new GenericType<String>() {
            }));
        } catch (Exception e) {
            LOGGER.error("#post Error : {}", e);
            webhookResponseDto.setHttpCode(HttpStatus.SC_BAD_GATEWAY);
        }
        return webhookResponseDto;
    }

    private static String getJSONStringFromMap(Map<String,String> map){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",map.get("id"));
        jsonObject.addProperty("resource_id",map.get("resource_id"));
        if(map.get("sender_id")!=null){
            jsonObject.addProperty("sender_id", map.get("sender_id"));
        }
        jsonObject.addProperty("event_name",map.get("event_name"));
        jsonObject.addProperty("subscription_id",map.get("subscription_id"));
        jsonObject.addProperty("event_id",map.get("event_id"));
        jsonObject.addProperty("timestamp",map.get("timestamp"));
        return jsonObject.toString();
    }

}
