package com.hly.o2o.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * 对应spring-web.xml里面的transactionManagement
 * 继承TransactionManagementConfigurer是因为要开启tx:annotation-driven注解的声明式事物
 *
 *
 */
@Configuration
//首先用@EnableTransactionManagement开启事物支持后
//在在service里的方法上使用注解@Transaction即可
@EnableTransactionManagement
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {
    @Autowired
    //注入DataSourceConfiguration里面的dataSource，通过其createDataSource方法获取
    private DataSource dataSource;
    @Override
    /**
     * 关于事物管理
     */
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
