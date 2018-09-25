package startup;

import com.lftechnology.machpay.webhook.service.EventService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class PendingEventWebhookTrigger {

    @Inject
    private EventService eventService;

    @PostConstruct
    public void initialize(){
        //eventService.rerunPendingEvents();
    }
}
