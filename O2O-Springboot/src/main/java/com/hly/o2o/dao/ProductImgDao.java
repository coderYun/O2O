package com.hly.o2o.dao;

import java.util.List;

import com.hly.o2o.entity.ProductImg;

public interface ProductImgDao {
	
	
	List<ProductImg> queryProductImgList(long productId);
	
	/**
	 * 批量添加商品图片详情
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	
	
	/**
	 * 根据商品productId删除商品下所有的商品详情图
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);

}
