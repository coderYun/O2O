package com.hly.o2o.config.dao;

import com.hly.o2o.util.DESUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

/**
 * 配置datasource到springIOC容器中去
 */
@Configuration
//配置mybatis mapper的扫描路径
@MapperScan("com.hly.o2o.dao")
public class DataSourceConfiguration {
    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    /**
     * 生成与spring-dao.xml对应的bean DataSource
     */
    @Bean(name="dataSource")
    public ComboPooledDataSource createDataSource() throws PropertyVetoException {
        //生成dataSource实例
        ComboPooledDataSource dataSource=new ComboPooledDataSource();
        //设置驱动
        dataSource.setDriverClass(jdbcDriver);
        //设置数据库链接url
        dataSource.setJdbcUrl(jdbcUrl);
        //设置数据库用户名
        dataSource.setUser(DESUtils.getDecryptString(jdbcUsername));
        //设置数据库密码
        dataSource.setPassword(DESUtils.getDecryptString(jdbcPassword));
        //配置c3p0的相关属性
        //连接池最大连接数
        dataSource.setMaxPoolSize(30);
        //连接池最小连接数
        dataSource.setMinPoolSize(10);
        //关闭后不自动commit
        dataSource.setAutoCommitOnClose(false);
        //连接超时时间限制
        dataSource.setCheckoutTimeout(10000);
        //连接失败重试次数
        dataSource.setAcquireRetryAttempts(2);
        return dataSource;

    }
}
