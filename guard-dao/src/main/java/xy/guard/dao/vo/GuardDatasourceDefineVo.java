package xy.guard.dao.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "guard_datasource_define")
public class GuardDatasourceDefineVo {
    public static final String COL_SYSTEM = "system";
    /**
     * 系统
     */
    @TableId(value = "system", type = IdType.INPUT)
    private String system;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 加密后的密码
     */
    @TableField(value = "pwd")
    private String pwd;

    /**
     * 驱动
     */
    @TableField(value = "driver_class")
    private String driverClass;

    /**
     * 数据库链接
     */
    @TableField(value = "url")
    private String url;

    public static final String COL_USERNAME = "username";

    public static final String COL_PWD = "pwd";

    public static final String COL_DRIVER_CLASS = "driver_class";

    public static final String COL_URL = "url";
}