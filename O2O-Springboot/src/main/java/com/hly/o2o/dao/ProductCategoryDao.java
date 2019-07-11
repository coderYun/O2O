package com.hly.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hly.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 通过shopId查找出该商店里所有的商品类别信息
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> queryProductCategoryList(long shopId);
	/**
	 * 批量新增商品类型
	 * @param productCategoryList
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);

}
