package com.hly.o2o.enums;

public enum LocalAuthStateEnum {
	LOGINGFAIL(-1,"用户名或者密码输入有误"),SUUCESS(0,"操作成功"),NULL_AUTH_INFO(-1006,"注册信息为空"),
	ONLY_ONE_ACOUNT(-1007,"最多只能绑定一个账号");
	private int state;
	private String stateInfo;
	private LocalAuthStateEnum(int state,String stateInfo){
		this.state=state;
		this.stateInfo=stateInfo;
	}
	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static LocalAuthStateEnum stateOf(int index) {
		for (LocalAuthStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

	
	

}
