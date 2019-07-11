package com.hly.o2o.enums;

public enum  AwardStateEnum {
    SUCCESS(1, "操作成功"),
    EMPTY(1001, "信息为空");
    private int state;
    private String stateInfo;

    private AwardStateEnum(int state, String stateInfo) {
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
    public static AwardStateEnum stateOf(int state){
        for(AwardStateEnum awardStateEnum:values()){
            if(state == awardStateEnum.getState()){
                return awardStateEnum;
            }
        }
        return null;
    }
}
