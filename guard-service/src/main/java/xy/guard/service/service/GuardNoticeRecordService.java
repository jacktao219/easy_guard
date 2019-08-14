package xy.guard.service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xy.guard.dao.dao.GuardNoticeRecordDao;
import xy.guard.dao.vo.GuardNoticeDefineVo;
import xy.guard.dao.vo.GuardNoticeRecordVo;
import xy.guard.service.config.GuardApolloConfig;

/**
 * 警告通知service
 *
 * @author Ambitor
 */
@Service
public class GuardNoticeRecordService extends ServiceImpl<GuardNoticeRecordDao, GuardNoticeRecordVo> {

    @Autowired
    private GuardApolloConfig guardApolloConfig;

    /**
     * 保存
     */
    public boolean save(GuardNoticeDefineVo guardNoticeDefine, String status, Integer guardRecordId, String remark) {
        GuardNoticeRecordVo record = new GuardNoticeRecordVo();
        LocalDateTime now = LocalDateTime.now();
        record.setCreateTime(now);
        record.setGuardRecordId(guardRecordId);
        record.setNoticeDefineId(guardNoticeDefine.getNoticeDefineId());
        record.setNoticeStatus(status);
        record.setNoticeRemark(remark);
        record.setUpdateTime(now);
        record.setWarnChannel(guardNoticeDefine.getWarnChannel());
        record.setWarnNoticeTarget(guardNoticeDefine.getWarnNoticeTarget());
        return save(record);
    }

    /**
     * 是否重复通知
     */
    public boolean repeatWarn(Integer noticeDefineId) {
        LocalDateTime repeatTime = LocalDateTime.now().plusHours(-guardApolloConfig.getRepeatWarnTime());
        LambdaQueryWrapper<GuardNoticeRecordVo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GuardNoticeRecordVo::getNoticeDefineId, noticeDefineId)
            .ge(GuardNoticeRecordVo::getCreateTime, repeatTime).orderByDesc(GuardNoticeRecordVo::getCreateTime)
            .last("LIMIT 1");
        return count(queryWrapper) > 0;
    }
}




