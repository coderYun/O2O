package com.hly.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import com.hly.o2o.entity.ShopCategory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCategoryDaoTest  {
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
