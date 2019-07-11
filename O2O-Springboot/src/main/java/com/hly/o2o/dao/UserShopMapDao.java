package com.hly.o2o.dao;

import com.hly.o2o.entity.UserShopMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserShopMapDao {
    /**
     * 根据查询条件分页返回用户店铺积分列表
     * @param userShopCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserShopMap> queryUserShopMapList(@Param("userShopCondition") UserShopMap userShopCondition,
                                           @Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);


    /**
     * 查询相同条件下的店铺积分记录总数
     * @param userShopCondition
     * @return
     */
    int queryUserShopMapCount(@Param("userShopCondition") UserShopMap userShopCondition);


    /**
     * 根据传入的userId和longId查询顾客消费积分记录
     * @param userId
     * @param shopId
     * @return
     */
    UserShopMap queryUserShopMap(@Param("userId") long userId,@Param("shopId") long shopId);

    /**
     * 添加一条用户消费积分记录信息
     * @param userShopMap
     * @return
     */
    int insertUserShopMap(UserShopMap userShopMap );


    /**
     * 更新一条顾客消费积分记录信息
     * @param userShopMap
     * @return
     */
    int updateUserShopMapPoint(UserShopMap userShopMap);




}
