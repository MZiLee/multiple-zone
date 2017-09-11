package com.multiple.zone.config.mybatise;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
* @Description mybatise配置
* @Author lichao
* @Date 2017年9月11日15:53:57
*/
@Configuration
@MapperScan(basePackages = "multiple.zone.dao")
public class MyBatisConfig /*implements TransactionManagementConfigurer*/ {

    @Autowired
    private Environment env;

    /**
     * 创建数据源
     */
    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        try {
            Properties props = new Properties();
            props.put("driverClassName", env.getProperty("jdbc.driverClassName"));
            props.put("url", env.getProperty("jdbc.url"));
            props.put("username", env.getProperty("jdbc.username"));
            props.put("password", env.getProperty("jdbc.password"));

            props.put("maxActive",env.getProperty("jdbc.pool.maxActive"));
            props.put("maxWait",env.getProperty("jdbc.pool.maxWait"));
            props.put("initialSize",env.getProperty("jdbc.pool.initialSize"));
            props.put("minIdle",env.getProperty("jdbc.pool.minIdle"));

            props.put("timeBetweenEvictionRunsMillis",env.getProperty("jdbc.pool.timeBetweenEvictionRunsMillis"));
            props.put("minEvictableIdleTimeMillis",env.getProperty("jdbc.pool.minEvictableIdleTimeMillis"));
            props.put("validationQuery",env.getProperty("jdbc.pool.validationQuery"));
            props.put("testWhileIdle",env.getProperty("jdbc.pool.testWhileIdle"));
            props.put("testOnBorrow",env.getProperty("jdbc.pool.testOnBorrow"));
            props.put("testOnReturn",env.getProperty("jdbc.pool.testOnReturn"));
            props.put("poolPreparedStatements",env.getProperty("jdbc.pool.poolPreparedStatements"));
            props.put("maxPoolPreparedStatementPerConnectionSize",env.getProperty("jdbc.pool.maxPoolPreparedStatementPerConnectionSize"));

            return DruidDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource ds) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(ds);
        fb.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));
        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));
        fb.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(env.getProperty("mybatis.configLocation")));
        return fb.getObject();
    }

    //事物控制
//    @Override
//    public PlatformTransactionManager annotationDrivenTransactionManager() {
//        return new DataSourceTransactionManager(getDataSource());
//    }
}
