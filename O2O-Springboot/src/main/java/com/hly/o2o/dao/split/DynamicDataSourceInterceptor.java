package com.hly.o2o.dao.split;

import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }) })
public class DynamicDataSourceInterceptor implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);
	private static final String regex = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] objects = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement) objects[0];
		String lookupKey = DynamicDataSourceHolder.DB_MASTER;
		boolean synchronizationActive = TransactionSynchronizationManager.isActualTransactionActive();
		if (synchronizationActive != true) {
			// 如果是读方法
			if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
				// selectId为自增id查询主键(SELECT LAST_INSERT_ID())方法，使用主库
				if (mappedStatement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
					lookupKey = DynamicDataSourceHolder.DB_MASTER;
				} else {
					BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(objects[1]);
					String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\r\\n]", " ");
					if (sql.matches(regex)) {
						// 增删改用主库
						lookupKey = DynamicDataSourceHolder.DB_MASTER;
					} else {
						// 读用从库
						lookupKey = DynamicDataSourceHolder.DB_SLAVE;
					}
				}

			}
			// 非事物的用主库
		} else {
			lookupKey = DynamicDataSourceHolder.DB_MASTER;

		}
		logger.debug("设置方法[{}] use [{}] Strategy,sqlCommanType[{}]", mappedStatement.getId(), lookupKey,
				mappedStatement.getSqlCommandType().name());
		DynamicDataSourceHolder.setDbType(lookupKey);

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		// TODO Auto-generated method stub
		// Exeutor在mybatis中是支持一系列的增删改查的操作的
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties arg0) {
		// TODO Auto-generated method stub

	}

}
