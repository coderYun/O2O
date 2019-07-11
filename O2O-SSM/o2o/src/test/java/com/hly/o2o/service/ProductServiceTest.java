package com.hly.o2o.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hly.o2o.BaseTest;
import com.hly.o2o.dao.ProductDao;
import com.hly.o2o.dto.ImageHolder;
import com.hly.o2o.dto.ProductExecution;
import com.hly.o2o.entity.Product;
import com.hly.o2o.entity.ProductCategory;
import com.hly.o2o.entity.ProductImg;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.enums.ProductStateEnum;
import com.hly.o2o.exceptions.ShopOperationException;
import com.hly.o2o.service.ProductService;

public class ProductServiceTest extends BaseTest {
	@Autowired
	private ProductService productService;

	@Test

	public void TestAddProduct() throws FileNotFoundException {
		// 创建shopId为1且shopCategoryId为1的商品实例并给其成员变量赋值
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(1L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("测试用例3333");
		product.setCreateTime(new Date());
		product.setPriority(20);
		product.setProductDesc("测试商品22");
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());

		// 创建缩略图文件流
		File thumbnailFile = new File("F:/image/ee.png");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnial = new ImageHolder(thumbnailFile.getName(), is);
		// 创建两个商品详情图并添加到商品信息里面去
		File productImg1 = new File("F:/image/watermark.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("F:/image/ee.png");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));

		// 添加并验证
		ProductExecution addProduct = productService.addProduct(product, thumbnial, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), addProduct.getState());

	}

	@Test
	public void testModifyProduct() throws FileNotFoundException, ShopOperationException {
		// 创建shopId为1且shopCategoryId为1的商品实例并给其成员变量赋值
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(1L);
		product.setShop(shop);
		product.setProductCategory(pc);
		product.setProductName("正式的商品1");
		product.setProductDesc("正式的商品1");
		product.setProductId(46L);
		// 创建缩略图文件流
		File thumbnailFile = new File("F:/image/xiaohuangren.jpg");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnial = new ImageHolder(thumbnailFile.getName(), is);
		// 创建两个商品详情图并添加到商品信息里面去
		File productImg1 = new File("F:/image/ee.png");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("F:/image/watermark.jpg");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
		//修改商品并验证
		ProductExecution pe = productService.modifyProduct(product, thumbnial, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());


	}

}
