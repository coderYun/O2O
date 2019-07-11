package com.hly.o2o.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.hly.o2o.BaseTest;
import com.hly.o2o.dao.ProductImgDao;
import com.hly.o2o.entity.ProductImg;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductImgDaoTest extends BaseTest {
	@Autowired
	private ProductImgDao productImgDao;

	@Test
	@Ignore
	public void testABatchInsertProductImg() throws Exception {
		// 为商品ID为1的店铺添加两种详情图片
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("图片1");
		productImg1.setImgDesc("测试图片1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(45L);
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("图片2");
		productImg1.setImgDesc("测试图片2");
		productImg2.setPriority(1);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(45L);
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2, effectedNum);
	}

	@Test
	@Ignore
	public void testCDeleteProductImgByProductId() throws Exception {
		long productId = 45;
		int effectNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectNum);
	}

	@Test
	public void testQueryImageListById() {
		long productId = 46;
		List<ProductImg> queryProductImgList = productImgDao.queryProductImgList(productId);
		assertEquals(2, queryProductImgList.size());
	}
}
