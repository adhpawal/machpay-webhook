package com.lftechnology.machpay.webhook.util;

import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adhpawal on 9/4/17.
 */
public class EncryptUtils {

    private static final String HMAC_HASH_ALGORITHM = "HmacSHA256";
    private static final String UTF_8 = "UTF-8";

    public static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance(HMAC_HASH_ALGORITHM);
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(UTF_8), HMAC_HASH_ALGORITHM);
        sha256_HMAC.init(secret_key);
        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes(UTF_8)));
    }

    public static String getJSONStringFromMap(Map<String,String> map){
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

    public static void main(String[] args) throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("id", String.valueOf("7057c062-b5cc-451f-a6fb-7f62d90ddc05"));
        requestBody.put("resource_id", "b8e63f63-a84a-43ad-a283-42e831440edb");
        requestBody.put("sender_id", "7d33c450-f771-4f5c-8dd6-8aec99f437f2");
        requestBody.put("event_name", "transaction_processed");
        requestBody.put("subscription_id", "6ac729de-ca9f-4322-9834-f1f132ced252");
        requestBody.put("event_id", String.valueOf("2004a08f-b808-4d10-bd1b-85f50b1f4a4a"));
        requestBody.put("timestamp", "2018-06-04T22:58:00.083");
        String val=getJSONStringFromMap(requestBody);
        System.out.println(val);
        System.out.println(encode("password", val));
    }
}
