package com.hly.o2o.dto;

import java.util.List;

import com.hly.o2o.entity.WechatAuth;
import com.hly.o2o.enums.WechatAuthStateEnum;

public class WechatAuthExecution {
  
	//结果状态
	private int state;
	//状态标识
	private String stateInfo;
	
	private int count;
	
	private WechatAuth wechatAuth;
	
	private List<WechatAuth> wechatAuthList;
	
	public WechatAuthExecution(){
		
	}
	
	//失败时的构造器
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public WechatAuth getWechatAuth() {
		return wechatAuth;
	}

	public void setWechatAuth(WechatAuth wechatAuth) {
		this.wechatAuth = wechatAuth;
	}

	public List<WechatAuth> getWechatAuthList() {
		return wechatAuthList;
	}

	public void setWechatAuthList(List<WechatAuth> wechatAuthList) {
		this.wechatAuthList = wechatAuthList;
	}

	public WechatAuthExecution(WechatAuthStateEnum authStateEnum){
		this.state=authStateEnum.getState();
		this.stateInfo=authStateEnum.getStateInfo();
		
	}
	//成功时的构造器
	public WechatAuthExecution(WechatAuthStateEnum authStateEnum,WechatAuth wechatAuth){
		this.state=authStateEnum.getState();
		this.stateInfo=authStateEnum.getStateInfo();
		this.wechatAuth=wechatAuth;
		
	}
	//成功时的构造器
	public WechatAuthExecution(WechatAuthStateEnum authStateEnum,List<WechatAuth> wechatAuthList){
		this.state=authStateEnum.getState();
		this.stateInfo=authStateEnum.getStateInfo();
		this.wechatAuthList=wechatAuthList;
	}
	
	
	
}