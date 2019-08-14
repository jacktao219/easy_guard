package xy.guard.dao.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import xy.common.core.encryption.ThreeDESUtil;
import xy.common.core.exception.BusinessException;
import xy.common.core.exception.CommonErrorCode;
import xy.common.core.util.spring.SpringContextUtils;
import xy.guard.dao.dao.GuardDatasourceDefineDao;
import xy.guard.dao.vo.GuardDatasourceDefineVo;

/**
 * 链接池 Created by Ambitor on 2019/8/8
 */
public final class DataSourceHolder {

    private DataSourceHolder() {
    }

    private static volatile Map<String, DruidDataSource> holder = new ConcurrentHashMap<>();

    public static DruidDataSource getDataSource(String system) {
        if (holder.get(system) == null) {
            register(system);
        }
        return holder.get(system);
    }

    private static synchronized void register(String system) {
        GuardDatasourceDefineDao guardDatasourceDefineDao = SpringContextUtils.getBean(GuardDatasourceDefineDao.class);
        GuardDatasourceDefineVo guardDatasourceDefine = guardDatasourceDefineDao.selectById(system);
        if (guardDatasourceDefine == null) {
            throw new BusinessException(CommonErrorCode.PARAM_ERROR, "未找到数据源配置{0}", system);
        }
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(guardDatasourceDefine.getUsername());
        String password = ThreeDESUtil.decrypt(guardDatasourceDefine.getPwd(), KEY);
        dataSource.setPassword(password);
        dataSource.setUrl(guardDatasourceDefine.getUrl());
        dataSource.setDriverClassName(guardDatasourceDefine.getDriverClass());
        dataSource.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
        dataSource.setMaxActive(MAX_ACTIVE);
        dataSource.setTestOnBorrow(true);
        dataSource.setInitialSize(INITIAL_SIZE);
        dataSource.setMinIdle(MIN_IDLE);
        dataSource.setValidationQuery("SELECT 1");
        holder.put(system, dataSource);
    }

    private static final String KEY = "123456788765432112345678";
    private static final int QUERY_TIMEOUT_SECONDS = 10;
    private static final int MAX_ACTIVE = 20;
    private static final int INITIAL_SIZE = 1;
    private static final int MIN_IDLE = 1;
}
