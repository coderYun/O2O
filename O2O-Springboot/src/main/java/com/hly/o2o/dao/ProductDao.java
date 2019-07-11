package com.hly.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hly.o2o.entity.Product;

public interface ProductDao {

	/**
	 * 通过商品ID(productId)查询唯一的商品信息
	 * 
	 * @param productId
	 * @return
	 */
	Product queryProductById(long productId);

	/**
	 * 为店铺插入商品
	 * 
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);

	/**
	 * 根据传入的商品修改商品信息
	 * 
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);
    /**
     * 查询该店铺下的所有商品列表，可分页，可输入的查询条件有：商品名(模糊),商品状态,店铺ID，商品类别
     * @param productCondition
     * @param rowIndex 从第几行开始
     * @param pageSize 一页多少行
     * @return
     */
	List<Product> queryProductList(@Param("productCondition") Product productCondition, @Param("rowIndex") int rowIndex,
                                   @Param("pageSize") int pageSize);
	/**
	 * 查询对商品的总数量
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);
	
	/**
	 * 删除商品的时候要先将删除的商品的商品类别ID置为空（因为有外键约束）
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);
	
	/**
	 * 删除商品
	 * @param productId
	 * @param shopId
	 * @return
	 */
	int deleteProduct(@Param("productId") long productId, @Param("shopId") long shopId);;

}
