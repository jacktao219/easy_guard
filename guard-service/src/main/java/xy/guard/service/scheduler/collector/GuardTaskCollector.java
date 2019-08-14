package xy.guard.service.scheduler.collector;

import it.sauronsoftware.cron4j.SchedulingPattern;
import it.sauronsoftware.cron4j.TaskCollector;
import it.sauronsoftware.cron4j.TaskTable;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xy.guard.dao.vo.GuardDefineVo;
import xy.guard.service.scheduler.task.GuardTask;
import xy.guard.service.service.GuardDefineService;

/**
 * 告警任务收集类
 * Created by Ambitor on 2019/8/12
 *
 * @author Ambitor
 */
@Slf4j
@Component
public class GuardTaskCollector implements TaskCollector {

    @Autowired
    private GuardDefineService guardDefineService;

    @Override
    public TaskTable getTasks() {
        List<GuardDefineVo> guardDefines = guardDefineService.list();
        TaskTable taskTable = new TaskTable();
        for (GuardDefineVo guardDefine : guardDefines) {
            SchedulingPattern pattern = new SchedulingPattern(guardDefine.getFrequency());
            taskTable.add(pattern, new GuardTask(guardDefine));
        }
        return taskTable;
    }
}
