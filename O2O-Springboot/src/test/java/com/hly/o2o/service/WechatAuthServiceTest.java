package com.hly.o2o.service;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.hly.o2o.dto.WechatAuthExecution;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.WechatAuth;
import com.hly.o2o.enums.WechatAuthStateEnum;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthServiceTest {
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@Test
	public void testRigster(){
		//新增一条微信账号
		WechatAuth wechatAuth=new WechatAuth();
		PersonInfo personInfo=new PersonInfo();
		String openId="hly";
		//给微信账号设置上用户信息，但是不设置上用户ID
		//希望创建微信账号的时候自动创建用户信息
		personInfo.setCreateTime(new Date());
		personInfo.setName("测试一下");
		personInfo.setUserType(1);
		wechatAuth.setOpenId(openId);
		wechatAuth.setPersonInfo(personInfo);
		wechatAuth.setCreateTime(new Date());
		WechatAuthExecution wae = wechatAuthService.register(wechatAuth);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(),wae.getState());
		//通过openId新增的wechatAuth
		wechatAuth=wechatAuthService.getWechatAuthByOpenId(openId);
		//打印出用户名看是否和预期相等
		System.out.println(wechatAuth.getPersonInfo().getName());
		
		
		
	}

}
