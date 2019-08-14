package xy.guard.dao.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = "xy.guard.dao", sqlSessionFactoryRef = "guardSqlSessionFactory")
public class GuardDataSourceConfig {
	@Bean(name = "guardDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.guard")
	@Primary
	public DataSource guardDataSource() throws Exception {
		DruidDataSource build = DruidDataSourceBuilder.create().build();
		build.setPasswordCallbackClassName(DbPasswordCallback.class.getName());
		return build;
	}

	@Bean(name = "guardSqlSessionFactory")
	@Primary
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
		bean.setDataSource(guardDataSource());
		bean.setMapperLocations(
			new PathMatchingResourcePatternResolver().getResources("classpath*:/xy/guard/dao/**.xml"));
		return bean.getObject();
	}

	@Bean(name = "guardTransactionManager")
	@Primary
	public DataSourceTransactionManager bizTransactionManager(@Qualifier("guardDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}