/**
 *
 */
package job;

import com.lftechnology.machpay.webhook.dto.WebhookRequestDto;
import com.lftechnology.machpay.webhook.service.ConfigService;
import com.lftechnology.machpay.webhook.service.WebhookRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Create Hourly Scheduler for Failed Webhooks.
 *
 * @author adhpawal
 */
@Stateless
public class WebhookTriggerJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebhookTriggerJob.class.getName());

    @Inject
    private ConfigService configService;

    @Inject
    private WebhookRequestService webhookRequestService;

    @Resource
    private TimerService timerService;

    @Asynchronous
    public void initialize(WebhookRequestDto webhookRequestDto) {
        LOGGER.info("WebhookTriggerJob#initialize Intialize Webhook Job");
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo((Serializable) webhookRequestDto);
        timerConfig.setPersistent(Boolean.TRUE);
        timerService.createCalendarTimer(setSchedule(), timerConfig);
    }

    private ScheduleExpression setSchedule() {
        LOGGER.info("WebhookTriggerJob#setSchedule. Set Schedule.");
        ScheduleExpression expression = new ScheduleExpression();
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime localDateTime = dateTime.plusMinutes(configService.getWebhookDelayInMinutes());
        expression.hour(localDateTime.getHour());
        expression.minute(localDateTime.getMinute());
        LOGGER.debug("WebhookTriggerJob#setSchedule. Job Scheduled for {}", localDateTime);
        return expression;
    }

    @Timeout
    public void timeOut(Timer timer) {
        LOGGER.info("WebhookTriggerJob#timeOut Job Timeout.");
        WebhookRequestDto webhookRequestDto = (WebhookRequestDto) timer.getInfo();
        try {
            webhookRequestService.triggerScheduledWebhook(webhookRequestDto);
        } catch (Exception e) {
            LOGGER.error("WebhookTriggerJob#timeOut Error while executing Job. Error Messaet {0}", e);
        }
    }
}




