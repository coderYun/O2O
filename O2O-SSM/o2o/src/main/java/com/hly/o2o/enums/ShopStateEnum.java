package com.hly.o2o.enums;

public enum ShopStateEnum {
	CHECK(0,"审核中"),OFFLINE(-1,"非法店铺"),SUCCESS(1,"操作成功"),
	PASS(2,"审核通过"),INNER_ERROR(-1001,"内部系统错误"),NULL_SHOPID(-1002,"ShopId为空"),Null_SHOP(-1003,"shop信息为空");
	private int state;
	private String stateInfo;
	
	
	
	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	private ShopStateEnum(int state,String stateInfo){
		this.state=state;
		this.stateInfo=stateInfo;
	}
	
	/**
	 * 依据传入的state返回对应的enum值
	 * 
	 */
	public static ShopStateEnum stateOf(int state){
		
		for(ShopStateEnum stateEnum:values()){
			if(stateEnum.getState()==state){
				return stateEnum;
			}
		}
		return null;
	}

}
