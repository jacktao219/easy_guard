package xy.guard.dao.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName(value = "guard_notice_group")
public class GuardNoticeGroupVo {
    @TableId(value = "notice_group_id", type = IdType.AUTO)
    private Integer noticeGroupId;

    /**
     * 告警组名称
     */
    @TableField(value = "notice_group_name")
    private String noticeGroupName;

    /**
     * 告警组编码
     */
    @TableField(value = "notice_group_code")
    private String noticeGroupCode;

    /**
     * 类型 message:短信 email:邮件
     */
    @TableField(value = "target_type")
    private String targetType;

    /**
     * 手机号或者邮箱
     */
    @TableField(value = "target_value")
    private String targetValue;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    public static final String COL_NOTICE_GROUP_NAME = "notice_group_name";

    public static final String COL_NOTICE_GROUP_CODE = "notice_group_code";

    public static final String COL_TARGET_TYPE = "target_type";

    public static final String COL_TARGET_VALUE = "target_value";

    public static final String COL_CREATE_TIME = "create_time";
}