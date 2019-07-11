package com.hly.o2o.dto;

import java.util.List;

import com.hly.o2o.entity.LocalAuth;
import com.hly.o2o.enums.LocalAuthStateEnum;

public class LocalAuthExecution {
	//结果状态
	private int state;
	//结果状态标识
	private String stateInfo;
	//账号数量
	private int count;
	
	private LocalAuth localAuth;
	
	private List<LocalAuth> localAuthList;
	
	public LocalAuthExecution(){
		
	}
	//失败时的构造器
	public LocalAuthExecution(LocalAuthStateEnum stateEnum){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	
	//成功时的构造器
	public LocalAuthExecution(LocalAuthStateEnum stateEnum,LocalAuth localAuth){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.localAuth=localAuth;
	}
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
	public LocalAuth getLocalAuth() {
		return localAuth;
	}
	public void setLocalAuth(LocalAuth localAuth) {
		this.localAuth = localAuth;
	}
	public List<LocalAuth> getLocalAuthList() {
		return localAuthList;
	}
	public void setLocalAuthList(List<LocalAuth> localAuthList) {
		this.localAuthList = localAuthList;
	}
	

}
