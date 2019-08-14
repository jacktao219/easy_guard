package xy.guard.service.scheduler.listener;

import it.sauronsoftware.cron4j.SchedulerListener;
import it.sauronsoftware.cron4j.TaskExecutor;
import java.text.MessageFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xy.guard.dao.vo.GuardDefineVo;
import xy.guard.dao.vo.GuardRecordVo;
import xy.guard.service.enumerate.EvaluateResult;
import xy.guard.service.scheduler.task.GuardTask;
import xy.guard.service.service.GuardRecordService;

/**
 * 监听器 Created by Ambitor on 2019/8/12
 *
 * @author Ambitor
 */
@Slf4j
@Component
public class GuardSchedulerListener implements SchedulerListener {
    @Autowired
    private GuardRecordService guardRecordService;

    @Override
    public void taskLaunching(TaskExecutor taskExecutor) {
        GuardTask task = (GuardTask) taskExecutor.getTask();
        GuardDefineVo guardDefine = task.getGuardDefine();
        GuardRecordVo guardRecord =
            guardRecordService.save(guardDefine, String.valueOf(0), EvaluateResult.init.name(), null);
        task.setGuardRecord(guardRecord);
        log.error("GuardScheduler Init guardName:{} ", guardDefine.getGuardName());
    }

    @Override
    public void taskSucceeded(TaskExecutor taskExecutor) {
        GuardTask task = (GuardTask) taskExecutor.getTask();
        GuardDefineVo guardDefine = task.getGuardDefine();
        GuardRecordVo guardRecord = task.getGuardRecord();
        Integer evaluateResult = Integer.parseInt(guardRecord.getEvaluateResult());
        guardRecord.setWarnNoticeMsg(MessageFormat.format(guardDefine.getWarnNoticeMsg(), evaluateResult));
        guardRecord.setExecResult(EvaluateResult.success.name());
        guardRecordService.updateById(guardRecord);
        log.error("GuardScheduler Success guardName:{} count:{} ", guardDefine.getGuardName(), evaluateResult);
    }

    @Override
    public void taskFailed(TaskExecutor taskExecutor, Throwable e) {
        GuardTask task = (GuardTask) taskExecutor.getTask();
        GuardDefineVo guardDefine = task.getGuardDefine();
        log.error("GuardScheduler Exception guardName:{}", guardDefine.getGuardName(), e);
        GuardRecordVo guardRecord = task.getGuardRecord();
        guardRecord.setExecResult(EvaluateResult.failed.name());
        guardRecord.setExecRemark(e.getMessage());
        guardRecordService.updateById(guardRecord);
    }

}
