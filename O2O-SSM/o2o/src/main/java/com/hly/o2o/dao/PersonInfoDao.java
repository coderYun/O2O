package com.hly.o2o.dao;

import com.hly.o2o.entity.PersonInfo;

public interface PersonInfoDao {

	/**
	 * 通过用户ID去查询用户信息
	 * @param userId
	 * @return
	 */
	PersonInfo queryPersonInfoById(Long userId);
	
	/**
	 * 添加用户信息
	 * @param personInfo
	 * @return
	 */
	int insertPersonInfo(PersonInfo personInfo);
}