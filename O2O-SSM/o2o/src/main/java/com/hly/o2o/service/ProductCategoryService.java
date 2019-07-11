package com.hly.o2o.service;

import java.util.List;

import com.hly.o2o.dto.ProductCategoryExecution;
import com.hly.o2o.entity.ProductCategory;
import com.hly.o2o.exceptions.ProductCategoryOperationException;

public interface ProductCategoryService {
	/**
	 * 通过shopId查找该店铺的所有商品类别
	 * 
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(long shopId);
    /**
     * 批量插入商品类别
     * @param productCategoryList
     * @return
     * @throws ProductCategoryOperationException
     */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException;
	/**
	 * 
	 * 将该类别下的商品ID置为空，在删除该商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId) 
			throws ProductCategoryOperationException;
	      

}