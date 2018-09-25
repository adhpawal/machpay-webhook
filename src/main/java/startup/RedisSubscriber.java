/**
 *
 */
package startup;

import com.lftechnology.machpay.webhook.redis.subscriber.JedisSubscriber;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class RedisSubscriber {

    @Inject
    private JedisSubscriber jedisSubscriber;

    @PostConstruct
    public void initialize() {
        jedisSubscriber.initialize();
    }
}
