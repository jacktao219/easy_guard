package xy.guard.service.scheduler.task;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.TypeReference;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xy.common.core.constant.ProjectCode;
import xy.common.core.exception.BusinessException;
import xy.common.core.exception.CommonErrorCode;
import xy.common.core.vo.http.BaseRequestVo;
import xy.common.web.http.HttpUtils;
import xy.guard.dao.datasource.DataSourceHolder;
import xy.guard.dao.vo.GuardDefineVo;
import xy.guard.dao.vo.GuardNoticeDefineVo;
import xy.guard.dao.vo.GuardNoticeGroupVo;
import xy.guard.dao.vo.GuardRecordVo;
import xy.guard.service.enumerate.GuardGroupType;
import xy.guard.service.enumerate.GuardType;
import xy.guard.service.enumerate.WarnNoticeResult;
import xy.guard.service.service.GuardNoticeDefineService;
import xy.guard.service.service.GuardNoticeGroupService;
import xy.guard.service.service.GuardNoticeRecordService;
import xy.msg.client.service.MessageClientService;
import xy.msg.client.vo.WarnMsgReqVo;

/**
 * 告警调度service Created by Ambitor on 2019/8/12
 *
 * @author Ambitor
 */
@Slf4j
@Component
public class GuardSchedulerService {
    public static final String GUARD_SERVER = "guard_server";
    @Autowired
    private GuardNoticeDefineService guardNoticeDefineService;
    @Autowired
    private MessageClientService messageClientService;
    @Autowired
    private GuardNoticeRecordService noticeRecordService;
    @Autowired
    private GuardNoticeGroupService guardNoticeGroupService;

    /**
     * 开始调度
     */
    public int dispatch(GuardDefineVo guardDefine, GuardRecordVo guardRecord) throws SQLException {

        log.info("GuardSchedulerService dispatch guardName:{} evaluateType:{} ", guardDefine.getGuardName(),
            guardDefine.getGuardEvaluate());

        int response = evaluateByType(guardDefine, guardRecord);

        log.info("GuardSchedulerService dispatch guardName:{} evaluateType:{} 结果:{} ", guardDefine.getGuardName(),
            guardDefine.getGuardEvaluate(), response);

        return response;
    }

    /**
     * 根据类型处理
     */
    private int evaluateByType(GuardDefineVo guardDefine, GuardRecordVo guardRecord) throws SQLException {
        int response;
        String warnNoticeMsg = guardDefine.getWarnNoticeMsg();
        String guardType = guardDefine.getGuardType();
        if (GuardType.sql.name().equals(guardType)) {
            response = queryDb(guardDefine);
            warnNoticeMsg = MessageFormat.format(warnNoticeMsg, response);
            if (!String.valueOf(response).equals(guardDefine.getEvaluateExpect())) {
                notice(guardDefine, guardRecord.getGuardRecordId(), warnNoticeMsg);
            }
        } else if (GuardType.http.name().equals(guardType)) {
            response = http(guardDefine);
            warnNoticeMsg = MessageFormat.format(warnNoticeMsg, response);
            if (!String.valueOf(response).equals(guardDefine.getEvaluateExpect())) {
                notice(guardDefine, guardRecord.getGuardRecordId(), warnNoticeMsg);
            }
        } else {
            throw new BusinessException(CommonErrorCode.SERVER_ERROR, "不支持的告警类型{0}", guardType);
        }
        return response;
    }

    /**
     * 拨测只需要判断HTTP Code为200
     */
    private Integer http(GuardDefineVo guardDefine) {
        try {
            HttpUtils.get(guardDefine.getGuardEvaluate(), new TypeReference<String>() {
            }.getType());
            return HttpStatus.SC_OK;
        } catch (Exception e) {
            log.error("告警拨测HTTP失败", e);
            return CommonErrorCode.REQUEST_ERROR;
        }
    }

    /**
     *
     * @param guardDefine
     * @return
     * @throws SQLException
     */
    private int queryDb(GuardDefineVo guardDefine) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DruidPooledConnection connection = null;
        try {
            String system = guardDefine.getSystem();
            DruidDataSource dataSource = DataSourceHolder.getDataSource(system);
            if (dataSource == null) {
                throw new BusinessException(CommonErrorCode.REQUEST_ERROR, "未找到数据源配置{0}", system);
            }
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(guardDefine.getGuardEvaluate());
            resultSet = preparedStatement.executeQuery();
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }

        }
    }

    /**
     * 通知
     */
    private void notice(GuardDefineVo guardDefine, Integer guardRecordId, String warnNoticeMsg) {
        List<GuardNoticeDefineVo> noticeDefines = guardNoticeDefineService.list(guardDefine.getGuardDefineId());
        BaseRequestVo<WarnMsgReqVo> reqVo = new BaseRequestVo<>();
        reqVo.setProjectCode(ProjectCode.xy.name());
        reqVo.setSystem(GUARD_SERVER);
        WarnMsgReqVo warnMsgReqVo = new WarnMsgReqVo();
        warnMsgReqVo.setContent(warnNoticeMsg);
        reqVo.setData(warnMsgReqVo);
        for (GuardNoticeDefineVo noticeDefine : noticeDefines) {
            String groupCode = noticeDefine.getWarnNoticeTarget();
            Integer noticeDefineId = noticeDefine.getNoticeDefineId();
            String guardName = guardDefine.getGuardName();
            Integer repeatGap = guardDefine.getRepeatGap();
            boolean repeatWarn = noticeRecordService.repeatWarn(noticeDefineId, repeatGap);
            if (repeatWarn) {
                log.info("最近有发送过信息不重复推送 guardDefine:{} target:{}", guardName, repeatGap);
                continue;
            }
            List<GuardNoticeGroupVo> groupVos = guardNoticeGroupService.list(groupCode);
            if (CollectionUtils.isEmpty(groupVos)) {
                log.warn("请配置告警组 guardDefine:{}  groupCode:{}", guardName, groupCode);
                continue;
            }
            for (GuardNoticeGroupVo groupVo : groupVos) {
                String target = groupVo.getTargetValue();
                String type = groupVo.getTargetType();
                if (GuardGroupType.mobile.name().equals(type)) {
                    warnMsgReqVo.setMobile(target);
                    guardByMessage(guardRecordId, reqVo, warnMsgReqVo, noticeDefine);
                } else {
                    log.warn("暂不支持除短信以外告警方式");
                }
            }
        }
    }

    /**
     * 短信通知
     */
    private void guardByMessage(Integer guardRecordId, BaseRequestVo<WarnMsgReqVo> reqVo, WarnMsgReqVo warnMsgReqVo,
        GuardNoticeDefineVo noticeDefine) {
        String target = warnMsgReqVo.getMobile();
        String guardName = noticeDefine.getGuardName();
        try {
            messageClientService.sendWarnMsg(reqVo);
            log.info("发送告警短信成功 target:{} guardName:{}", target, guardName);
            noticeRecordService.save(noticeDefine, target, WarnNoticeResult.success.name(), guardRecordId, null);
        } catch (Exception e) {
            log.error("发送短信失败 target:{} guardName:{}", target, guardName);
            noticeRecordService
                .save(noticeDefine, target, WarnNoticeResult.failed.name(), guardRecordId, e.getMessage());
        }
    }
}
