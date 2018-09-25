package com.lftechnology.machpay.webhook.constants;

/**
 * @author adhpawal
 */
public class CommonConstant {

    public static final String HTTP_CLIENT_ID = "X-Client-Id";

    public static final String HTTP_CLIENT_SECRET = "X-Client-Secret";

    public static final String WEBHOOK_HEADER_SIGNATURE = "X-Raas-Webhook-Signature";

    public static final String WEBHOOK_HEADER_TOPIC = "X-Raas-Event";

    public static final Integer HTTP_OK = 200;

    private CommonConstant() {
    }
}
