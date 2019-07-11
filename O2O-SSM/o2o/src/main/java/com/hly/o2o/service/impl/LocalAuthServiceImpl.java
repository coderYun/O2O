package com.hly.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hly.o2o.dao.LocalAuthDao;
import com.hly.o2o.dto.LocalAuthExecution;
import com.hly.o2o.entity.LocalAuth;
import com.hly.o2o.enums.LocalAuthStateEnum;
import com.hly.o2o.exceptions.LocalAuthOperationException;
import com.hly.o2o.service.LocalAuthService;
import com.hly.o2o.util.MD5;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
	@Autowired
	private LocalAuthDao localAuthDao;

	@Override
	public LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password) {
		return localAuthDao.queryLocalByUserNameAndPwd(userName, MD5.getMd5(password));
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		return localAuthDao.queryLocalByUserId(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuhth(LocalAuth localAuth) throws LocalAuthOperationException {
		// 空值判断，传入的LocalAuth账号密码，用户信息特别是UserId不能为空，否则直接返回错误结果
		if (localAuth == null || localAuth.getUsername() == null || localAuth.getPassword() == null
				|| localAuth.getPersonInfo().getUserId() == null || localAuth.getPersonInfo() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		// 查询该用户是否已经绑定过本平台账号
		LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
		if (tempAuth != null) {
			// 如果该用户之前绑定过该平台就直接退出
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACOUNT);
		}

		// 如果之前没有绑定本平台账号，则创建一个平台账号与用户绑定
		try {
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			// 对密码进行MD5加密
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectNum = localAuthDao.insertLocalAuth(localAuth);
			// 判断是否创建成功
			if (effectNum <= 0) {
				throw new LocalAuthOperationException("绑定账号失败");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUUCESS, localAuth);

			}

		} catch (Exception e) {
			throw new LocalAuthOperationException("insert localAuth error" + e.getMessage());

		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException {
		// 非空判断，判断传入的用户密码，账号,userId是否为空，新旧密码是否相同，不满足条件直接返回错误信息
		if (userId != null && username != null && password != null && !password.equals(newPassword)) {
			try {
				// 更新密码
				int effectNum = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password),
						MD5.getMd5(newPassword), new Date());
				if (effectNum <= 0) {
					throw new LocalAuthOperationException("修改密码失败");
				} else {
					return new LocalAuthExecution(LocalAuthStateEnum.SUUCESS);

				}

			} catch (Exception e) {	
				throw new LocalAuthOperationException("修改密码失败" + e.getMessage());

			}

		} else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}

	}

}
