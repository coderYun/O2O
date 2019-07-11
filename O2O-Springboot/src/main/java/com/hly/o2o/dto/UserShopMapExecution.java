package com.hly.o2o.dto;

import com.hly.o2o.entity.ProductImg;
import com.hly.o2o.entity.UserProductMap;
import com.hly.o2o.entity.UserShopMap;
import com.hly.o2o.enums.UserShopMapStateEnum;

import java.util.List;

public class UserShopMapExecution {
    //状态标识
    private int state;
    //状态标识信息
    private String stateInfo;
    //数目
    private int count;

    private UserShopMap userShopMap;

    private List<UserShopMap> userShopMapList;

    public UserShopMapExecution() {

    }

    //失败时的构造器
    public UserShopMapExecution(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    //成功时的构造器
    public UserShopMapExecution(UserShopMapStateEnum stateEnum, UserShopMap userShopMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userShopMap = userShopMap;
    }

    //成功时的构造器
    public UserShopMapExecution(UserShopMapStateEnum stateEnum, List<UserShopMap> userShopMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userShopMapList = userShopMapList;
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

    public UserShopMap getUserShopMap() {
        return userShopMap;
    }

    public void setUserShopMap(UserShopMap userShopMap) {
        this.userShopMap = userShopMap;
    }

    public List<UserShopMap> getUserShopMapList() {
        return userShopMapList;
    }

    public void setUserShopMapList(List<UserShopMap> userShopMapList) {
        this.userShopMapList = userShopMapList;
    }
}
