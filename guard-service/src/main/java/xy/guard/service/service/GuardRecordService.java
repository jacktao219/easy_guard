package xy.guard.service.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import xy.common.core.exception.BusinessException;
import xy.common.core.exception.CommonErrorCode;
import xy.guard.dao.dao.GuardRecordDao;
import xy.guard.dao.vo.GuardDefineVo;
import xy.guard.dao.vo.GuardRecordVo;

/**
 * 告警记录service
 *
 * @author Ambitor
 */
@Service
public class GuardRecordService extends ServiceImpl<GuardRecordDao, GuardRecordVo> {

    /**
     * 保存
     */
    public GuardRecordVo save(GuardDefineVo guardDefine, String evaluateResult, String execResult, String execRemark) {
        LocalDateTime now = LocalDateTime.now();
        GuardRecordVo record = new GuardRecordVo();
        record.setCreateTime(now);
        record.setUpdateTime(now);
        record.setEvaluateExpect(guardDefine.getEvaluateExpect());
        record.setEvaluateResult(evaluateResult);
        record.setGuardName(guardDefine.getGuardName());
        record.setExecResult(execResult);
        record.setExecRemark(execRemark);
        record.setFrequency(guardDefine.getFrequency());
        record.setGuardDefineId(guardDefine.getGuardDefineId());
        record.setGuardEvaluate(guardDefine.getGuardEvaluate());
        record.setGuardType(guardDefine.getGuardType());
        record.setSystem(guardDefine.getSystem());
        if (!save(record)) {
            throw new BusinessException(CommonErrorCode.SERVER_ERROR, "添加数据库失败");
        }
        return record;
    }

}




