package com.hly.o2o.dao;

import com.hly.o2o.entity.ShopAuthMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopAuthMapDao {


    /**
     * 分页查询出该店铺下的所有分页信息
     * @param shopId
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<ShopAuthMap> queryShopAuthMapListByShopId(@Param("shopId") long shopId,
                                                   @Param("rowIndex") int rowIndex,@Param("pageSize") int pageSize);


    /**
     * 获取授权总数
     * @param shopId
     * @return
     */
    int queryShopAuthCountByShopId(@Param("shopId") long shopId);


    /**
     * 新增一条店铺与操作员的映射关系
     * @param shopAuthMap
     * @return
     */
    int insertShopAuthMap(ShopAuthMap shopAuthMap);

    /**
     * 更新店铺与操作员的映射关系信息
     * @param shopAuthMap
     * @return
     */
    int updateShopAuthMap(ShopAuthMap shopAuthMap);


    /**
     * 对某个员工进行除权处理
     * @param shopAuthId
     * @return
     */
    int deleteShopAuthMap(long shopAuthId);

    /**
     * 通过shopAuthId查询某个员工的信息
     * @param shopAuthId
     * @return
     */
    ShopAuthMap queryShopAuthMapById(Long shopAuthId);



}
