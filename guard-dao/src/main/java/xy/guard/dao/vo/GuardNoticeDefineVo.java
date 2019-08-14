package xy.guard.dao.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName(value = "guard_notice_define")
public class GuardNoticeDefineVo {
    public static final String COL_WARN_NOTICE_MSG = "warn_notice_msg";
    @TableId(value = "notice_define_id", type = IdType.AUTO)
    private Integer noticeDefineId;

    @TableField(value = "guard_define_id")
    private Integer guardDefineId;

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

    public static final String COL_GUARD_DEFINE_ID = "guard_define_id";

    public static final String COL_WARN_CHANNEL = "warn_channel";

    public static final String COL_WARN_NOTICE_TARGET = "warn_notice_target";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}