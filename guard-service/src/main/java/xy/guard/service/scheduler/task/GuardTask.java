package xy.guard.service.scheduler.task;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import java.sql.SQLException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xy.common.core.exception.BusinessException;
import xy.common.core.exception.CommonErrorCode;
import xy.common.core.util.spring.SpringContextUtils;
import xy.guard.dao.vo.GuardDefineVo;
import xy.guard.dao.vo.GuardRecordVo;

/**
 * Created by Ambitor on 2019/8/12
 *
 * @author Ambitor
 */
@Slf4j
@Setter
@Getter
public class GuardTask extends Task {

    private GuardDefineVo guardDefine;

    private GuardRecordVo guardRecord;

    public GuardTask(GuardDefineVo guardDefine) {
        this.guardDefine = guardDefine;
    }

    @Override
    public void execute(TaskExecutionContext context) throws RuntimeException {
        try {
            log.info("GuardTask execute guardName:{} guardRecord:{}", guardDefine.getGuardName(),
                guardRecord.getGuardRecordId());
            GuardSchedulerService guardSchedulerService = SpringContextUtils.getBean(GuardSchedulerService.class);
            int response = guardSchedulerService.dispatch(guardDefine, guardRecord);
            if (guardRecord != null) {
                guardRecord.setEvaluateResult(String.valueOf(response));
            }
        } catch (SQLException e) {
            throw new BusinessException(e, CommonErrorCode.SERVER_ERROR, e.getMessage());
        }
    }

}
