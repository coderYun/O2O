package com.hly.o2o.dao.split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceHolder {
	//先打印日志记录
	private static Logger logger=LoggerFactory.getLogger(DynamicDataSourceHolder.class);
	//ThreadLocal是线程安全的
	private static ThreadLocal<String> contextHolder=new ThreadLocal<String>();
	public static final String DB_MASTER="master";
	public static final String DB_SLAVE="slave";
	/**
	 *  获取线程的DbType
	 * @return
	 */
	public static String getDbType(){
		String db=contextHolder.get();
		if(db==null){
			db=DB_MASTER;
			
		}
		return db;
	}
	/**
	 * 设置线程的DbType
	 * @param str
	 */
	public static void setDbType(String str){
		logger.debug("所使用的数据源是"+str);
		contextHolder.set(str);
		
	}
	
	//清空ThreadLocal对象（清理连接类型）
	public static void clearDBType(){
		contextHolder.remove();
	}

}
