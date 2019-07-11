package com.hly.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hly.o2o.entity.Shop;

public interface ShopDao {
	/**
	 * 分页查询店铺:可输入的条件有:店铺名称（模糊），店铺状态，店铺类别，区域ID，Owner
	 * @param shop 查询的条件
	 * @param rowIndex 从第几行开始读取数据
	 * @param pageSize 返回的条数
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);
	
	/**
	 * 返回queryListshop 的总数
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);

	/**
	 * 查询店铺，通过店铺ID查询
	 * 
	 * 
	 */
	Shop queryByShopId(long shopId);

	/**
	 * 新增店铺 返回-1插入不成功，返回1插入成功
	 */
	int insertShop(Shop shop);

	/**
	 * 更新店铺信息
	 * 
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);

}
