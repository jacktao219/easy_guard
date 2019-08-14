package xy.guard.dao.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName(value = "guard_notice_record")
public class GuardNoticeRecordVo {
    public static final String COL_WARN_NOTICE_MSG = "warn_notice_msg";
    @TableId(value = "notice_record_id", type = IdType.AUTO)
    private Integer noticeRecordId;

    @TableField(value = "notice_define_id")
    private Integer noticeDefineId;

    @TableField(value = "guard_record_id")
    private Integer guardRecordId;

    /**
     * 通知到达
     */
    @TableField(value = "notice_status")
    private String noticeStatus;

    /**
     * 通知描述
     */
    @TableField(value = "notice_remark")
    private String noticeRemark;

    /**
     * message:短信 email:邮件
     */
    @TableField(value = "warn_channel")
    private String warnChannel;

    /**
     * 告警对象
     */
    @TableField(value = "warn_notice_target")
    private String warnNoticeTarget;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    public static final String COL_NOTICE_DEFINE_ID = "notice_define_id";

    public static final String COL_GUARD_RECORD_ID = "guard_record_id";

    public static final String COL_NOTICE_STATUS = "notice_status";

    public static final String COL_NOTICE_REMARK = "notice_remark";

    public static final String COL_WARN_CHANNEL = "warn_channel";

    public static final String COL_WARN_NOTICE_TARGET = "warn_notice_target";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}