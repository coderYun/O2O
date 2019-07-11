package com.hly.o2o.dto;

import com.hly.o2o.entity.ShopAuthMap;
import com.hly.o2o.enums.ShopAuthMapStateEnum;

import java.util.List;

public class ShopAuthMapExecution {

    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //授权数
    private Integer count;

    //操作的shopAuthMap
   private ShopAuthMap shopAuthMap;

   //授权列表，查询专用
    private List<ShopAuthMap> shopAuthMapList;

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

    public ShopAuthMap getShopAuthMap() {
        return shopAuthMap;
    }

    public void setShopAuthMap(ShopAuthMap shopAuthMap) {
        this.shopAuthMap = shopAuthMap;
    }

    public List<ShopAuthMap> getShopAuthMapList() {
        return shopAuthMapList;
    }

    public void setShopAuthMapList(List<ShopAuthMap> shopAuthMapList) {
        this.shopAuthMapList = shopAuthMapList;
    }

    public ShopAuthMapExecution(){

    }

    //失败时的构造器
    public ShopAuthMapExecution(ShopAuthMapStateEnum shopAuthMapStateEnum){

        this.state=shopAuthMapStateEnum.getState();
        this.stateInfo=shopAuthMapStateEnum.getStateInfo();
    }

    //成功时的构造器
    public ShopAuthMapExecution(ShopAuthMapStateEnum shopAuthMapStateEnum,ShopAuthMap shopAuthMap){
        this.state=shopAuthMapStateEnum.getState();
        this.stateInfo=shopAuthMapStateEnum.getStateInfo();
        this.shopAuthMap=shopAuthMap;
    }


    //成功时的构造器
    public ShopAuthMapExecution(ShopAuthMapStateEnum shopAuthMapStateEnum,List<ShopAuthMap> shopAuthMapList){
        this.state=shopAuthMapStateEnum.getState();
        this.stateInfo=shopAuthMapStateEnum.getStateInfo();
        this.shopAuthMapList=shopAuthMapList;
    }
}
