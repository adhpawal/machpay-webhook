package com.lftechnology.machpay.webhook.redis.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lftechnology.machpay.common.dto.event.SubscriptionEvent;
import com.lftechnology.machpay.redis.RedisFactory;
import com.lftechnology.machpay.webhook.service.ConfigService;
import com.lftechnology.machpay.webhook.service.IncomingEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PreDestroy;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Handles Functionality Related to REDIS subscription.
 *
 * <p>
 * Created by adhpawal on 9/4/17.
 */

@Singleton
public class JedisSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisSubscriber.class);
    private static JedisPool jedisPool = RedisFactory.getInstance().getJedisPool();

    @Inject
    private IncomingEventService incomingEventService;

    @Inject
    private ConfigService configService;

    private static final String BACKUP="_backup";
    private static final String EVENTS="_events";

    @Asynchronous
    public void initialize() {
        setupSubscriber();
    }

    @PreDestroy
    public void destroy() {
        jedisPool.destroy();
    }

    private JedisPubSub setupSubscriber() {
        final JedisPubSub jedisPubSub = new JedisPubSub() {

            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                LOGGER.info("#onSubscribe ChannelName : {}", channel);
                Jedis jedis=null;
                try{
                    jedis = jedisPool.getResource();
                    while (jedis.llen(channel+EVENTS) > 0) {
                        String result = jedis.rpop(channel+EVENTS);
                        LOGGER.info("#onSubscribe ChannelName : {} Queued Message : {}", channel, result);
                        try {
                            SubscriptionEvent subscriptionEvent = new ObjectMapper().readValue(result, SubscriptionEvent.class);
                            incomingEventService.registerEvent(subscriptionEvent);
                        } catch (Exception e) {
                            LOGGER.error("#onSubscribe ChannelName : {} Error : {}", channel, e);
                        }
                    }
                }finally {
                    if(jedis!=null)
                        jedis.close();
                }
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPSubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPMessage(String pattern, String channel, String message) {
            }

            @Override
            public void onMessage(String channel, String message) {
                LOGGER.info("#onMessage ChannelName : {} Message : {}", channel, message);
                try {
                    Thread.sleep(10);
                    Jedis jedisClient=null;
                    try {
                        jedisClient = jedisPool.getResource();
                        String messageFromStorage = jedisClient.lpop(channel+EVENTS);
                        if(messageFromStorage!=null){
                            processRequest(messageFromStorage);
                        }
                    } catch (Exception e) {
                        LOGGER.error("#onMessage ChannelName : {} Error : {}", channel, e);
                    }finally {
                        jedisClient.close();
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("#onMessage ChannelName : {} Error : {}", channel, e);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LOGGER.info("JedisSubscriber#setupSubscriber Subscription Thread");
                    Jedis jedis = jedisPool.getResource();
                    LOGGER.info("JedisSubscriber#setupSubscriber Subscription Thread :{}", configService.getChannelName());
                    jedis.subscribe(jedisPubSub, configService.getChannelName());
                    jedis.quit();
                } catch (Exception e) {
                    LOGGER.error("JedisSubscriber#setupSubscriber Subscription Thread : {}", e);
                }
            }
        }, "subscriberThread").start();
        return jedisPubSub;
    }

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void processRequest(String request) {
        LOGGER.info("JedisSubscriber#processRequest Message : {}", request);
        ObjectMapper mapper = new ObjectMapper();
        SubscriptionEvent eventDto;
        try {
            eventDto = mapper.readValue(request, SubscriptionEvent.class);
            incomingEventService.registerEvent(eventDto);
        } catch (IOException e) {
            LOGGER.error("JedisSubscriber#processRequest IOException When Parsing Incoming Request : {}", request);
        }
    }
}
