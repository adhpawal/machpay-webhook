package com.lftechnology.machpay.webhook.dto;

/**
 * Created by adhpawal on 9/4/17.
 */
public class WebhookResponseDto {

    String requestBody;

    String responseBody;

    Integer httpCode;

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }


}
