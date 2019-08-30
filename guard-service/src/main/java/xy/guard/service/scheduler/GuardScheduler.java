package xy.guard.service.scheduler;

import it.sauronsoftware.cron4j.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xy.guard.service.scheduler.collector.GuardTaskCollector;
import xy.guard.service.scheduler.listener.GuardSchedulerListener;

/**
 * 告警调度器 Created by Ambitor on 2019/8/8
 *
 * @author Ambitor
 */
@Slf4j
@Component
public class GuardScheduler implements InitializingBean {

    @Autowired
    private GuardTaskCollector guardTaskCollector;
    @Autowired
    private GuardSchedulerListener guardSchedulerListener;

    @Override
    public void afterPropertiesSet() {
        // Creates a Scheduler instance.
        Scheduler s = new Scheduler();
        // Task Collector
        s.addTaskCollector(guardTaskCollector);
        // Daemon Thread
        s.setDaemon(true);
        // Listener
        s.addSchedulerListener(guardSchedulerListener);
        // Starts the scheduler.
        s.start();
    }

}
