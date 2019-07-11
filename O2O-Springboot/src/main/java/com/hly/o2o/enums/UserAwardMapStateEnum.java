package com.hly.o2o.enums;

public enum UserAwardMapStateEnum {
    SUCCESS(1, "操作成功"),FAIL(-1002,"操作失败"),
    NULL_USER_AWARD_MAP(-1001, "信息为空");

    private int state;
    private String stateInfo;

    UserAwardMapStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    /**
     * 依据传入的state返回相应的enum值
     * @param state
     * @return
     */
    public static UserAwardMapStateEnum stateOf(int state){
        for(UserAwardMapStateEnum userShopMapStateEnum:values()){
            if(state == userShopMapStateEnum.getState()){
                return userShopMapStateEnum;
            }
        }
        return null;
    }
}
