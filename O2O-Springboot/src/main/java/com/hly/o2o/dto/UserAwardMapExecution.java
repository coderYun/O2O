package com.hly.o2o.dto;

import com.hly.o2o.entity.UserAwardMap;
import com.hly.o2o.enums.UserAwardMapStateEnum;

import java.util.List;

public class UserAwardMapExecution {
    /**
     * 结果状态
     */
    private int state;
    /**
     * 状态标识
     */
    private String stateInfo;
    /**
     * 数目
     */
    private Integer count;
    /**
     * 操作的userProductMap
     */
    private UserAwardMap userAwardMap;
    /**
     * 用户在某商店消费积分列表
     */
    private List<UserAwardMap> userAwardMapList;

    public UserAwardMapExecution() {
    }

    /**
     * 失败时构造器
     *
     * @param userAwardMapStateEnum
     */
    public UserAwardMapExecution(UserAwardMapStateEnum userAwardMapStateEnum) {
        this.state = userAwardMapStateEnum.getState();
        this.stateInfo = userAwardMapStateEnum.getStateInfo();
    }

    /**
     * 成功时构造器
     *
     * @param userAwardMapStateEnum
     * @param userAwardMap
     */
    public UserAwardMapExecution(UserAwardMapStateEnum userAwardMapStateEnum, UserAwardMap userAwardMap) {
        this.state = userAwardMapStateEnum.getState();
        this.stateInfo = userAwardMapStateEnum.getStateInfo();
        this.userAwardMap = userAwardMap;
    }

    /**
     * 成功时构造器
     *
     * @param userAwardMapStateEnum
     * @param userAwardMapList
     */
    public UserAwardMapExecution(UserAwardMapStateEnum userAwardMapStateEnum, List<UserAwardMap> userAwardMapList) {
        this.state = userAwardMapStateEnum.getState();
        this.stateInfo = userAwardMapStateEnum.getStateInfo();
        this.userAwardMapList = userAwardMapList;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserAwardMap getUserAwardMap() {
        return userAwardMap;
    }

    public void setUserAwardMap(UserAwardMap userAwardMap) {
        this.userAwardMap = userAwardMap;
    }

    public List<UserAwardMap> getUserAwardMapList() {
        return userAwardMapList;
    }

    public void setUserAwardMapList(List<UserAwardMap> userAwardMapList) {
        this.userAwardMapList = userAwardMapList;
    }
}

