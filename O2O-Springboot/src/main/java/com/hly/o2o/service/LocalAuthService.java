package com.hly.o2o.service;

import com.hly.o2o.dto.LocalAuthExecution;
import com.hly.o2o.entity.LocalAuth;
import com.hly.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {

	/**
	 * 通过用户名和密码获取平台账号信息
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password);

	/**
	 * 通过用户Id获取平台账号
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 * 绑定本平台账号
	 * 
	 * @param localAuth
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution bindLocalAuhth(LocalAuth localAuth) throws LocalAuthOperationException;

	/**
	 * 修改平台账号密码
	 * 
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException;

}