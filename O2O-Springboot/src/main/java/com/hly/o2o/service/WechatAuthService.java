package com.hly.o2o.service;

import com.hly.o2o.dto.WechatAuthExecution;
import com.hly.o2o.entity.WechatAuth;
import com.hly.o2o.exceptions.WechatAuthOperationException;

public interface WechatAuthService {

	/**
	 * 通过openId找到微信公众号里相对应的微信账号
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);
	/**
	 * 注册本平台的微信账号
	 * @param wechatAuth
	 * @return
	 * @throws WechatAuthOperationException
	 */

	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;

}
