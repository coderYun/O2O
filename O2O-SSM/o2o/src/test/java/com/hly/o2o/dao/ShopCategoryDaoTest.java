package com.hly.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hly.o2o.BaseTest;
import com.hly.o2o.dao.ShopCategoryDao;
import com.hly.o2o.entity.ShopCategory;
public class ShopCategoryDaoTest extends BaseTest {
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	@Test
	public void testQueryShopCtegory(){
		List<ShopCategory> shopCategoryList=shopCategoryDao.queryShopCategory(null);
		System.out.println(shopCategoryList.size());
		
		//assertEquals(1,shopCategoryList.size());
		
		
		/*
		ShopCategory parentShopCategory=new ShopCategory();
		ShopCategory testShopCategory=new ShopCategory();
		parentShopCategory.setShopCategoryId(1L);
		testShopCategory.setParent(parentShopCategory);
		List<ShopCategory> shopCategoryList2=shopCategoryDao.queryShopCategory(testShopCategory);
		assertEquals(1,shopCategoryList2.size());
		System.out.println(shopCategoryList2.get(0).getShopCategoryName());*/
	}

}
