package xy.guard.dao.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName(value = "guard_define")
public class GuardDefineVo {
    public static final String COL_FREQUENCY_UNIT = "frequency_unit";
    public static final String COL_NEXT_EXEC_TIME = "next_exec_time";
    @TableId(value = "guard_define_id", type = IdType.AUTO)
    private Integer guardDefineId;

    /**
     * 所属系统
     */
    @TableField(value = "system")
    private String system;

    /**
     * 告警名称
     */
    @TableField(value = "guard_name")
    private String guardName;

    /**
     * 告警类型 sql:数据库sql配置
     */
    @TableField(value = "guard_type")
    private String guardType;

    /**
     * 告警评估，如：sql语句的配置
     */
    @TableField(value = "guard_evaluate")
    private String guardEvaluate;

    /**
     * 评估期望结果，如果不等于此结果就告警
     */
    @TableField(value = "evaluate_expect")
    private String evaluateExpect;

    /**
     * 频率
     */
    @TableField(value = "frequency")
    private String frequency;

    /**
     * 多少分钟内不重复告警，默认30分钟
     */
    @TableField(value = "repeat_gap")
    private Integer repeatGap;

    /**
     * 告警内容
     */
    @TableField(value = "warn_notice_msg")
    private String warnNoticeMsg;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    public static final String COL_SYSTEM = "system";

    public static final String COL_GUARD_NAME = "guard_name";

    public static final String COL_GUARD_TYPE = "guard_type";

    public static final String COL_GUARD_EVALUATE = "guard_evaluate";

    public static final String COL_EVALUATE_EXPECT = "evaluate_expect";

    public static final String COL_FREQUENCY = "frequency";

    public static final String COL_REPEAT_GAP = "repeat_gap";

    public static final String COL_WARN_NOTICE_MSG = "warn_notice_msg";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}