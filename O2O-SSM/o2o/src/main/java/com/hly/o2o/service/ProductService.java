package com.hly.o2o.service;

import java.io.InputStream;
import java.util.List;

import com.hly.o2o.dto.ImageHolder;
import com.hly.o2o.dto.ProductExecution;
import com.hly.o2o.entity.Product;
import com.hly.o2o.exceptions.ProductOperationException;

public interface ProductService {

	/**
	 * 
	 * 添加商品信息以及图片处理
	 * 
	 * @param product
	 * @param thumbnial
	 *            缩略图
	 * @param productImgList
	 *            详情图
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnial, List<ImageHolder> productImgList)
			throws ProductOperationException;

	/**
	 * 通过商品ID查询唯一商品
	 * 
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);

	/**
	 * 修改商品详情以及图片处理
	 * 
	 * @param product
	 *            商品
	 * @param thumbnail
	 *            商品缩略图文件流
	 * @param productImageHolderList
	 *            商品详情图文件流
	 * @return
	 */
	ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImageHolderList);

	/**
	 * 查询商品列表并分页，可输入的条件:名字(模糊查询),商品状态，店铺ID，商品类别
	 * 
	 * @param productCondition
	 * @param pageIndex
	 *            那一页的数据
	 * @param pageSize
	 *            一页有多少数据
	 * @return
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
    /**
     * 删除商品
     * @param productId
     * @param shopId
     * @return
     * @throws ProductOperationException
     */
	ProductExecution deleteProduct(Long productId, Long shopId) throws ProductOperationException;

}
