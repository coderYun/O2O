package com.hly.o2o.dao;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hly.o2o.BaseTest;
import com.hly.o2o.dao.ShopDao;
import com.hly.o2o.entity.Area;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.ShopCategory;
import com.hly.o2o.util.PathUtil;

public class ShopDaoTest extends BaseTest {
	@Autowired
	private ShopDao shopDao;
	@Test
	public void testqueryShopListAndCount(){
		Shop shopCondition=new Shop();
		ShopCategory childCategory=new ShopCategory();
		ShopCategory parentCategory=new ShopCategory();
		parentCategory.setShopCategoryId(9L);
		childCategory.setParent(parentCategory);
		shopCondition.setShopCategory(childCategory);
		/*PersonInfo owner=new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);*/
		List<Shop> queryShopList = shopDao.queryShopList(shopCondition, 0,2);
		int queryShopCount = shopDao.queryShopCount(shopCondition);
		System.out.println("店铺的列表大小为+======="+queryShopList.size());
		System.out.println("店铺的总数量+======="+queryShopCount);
		
	}
	
	
    @Ignore
	@Test
	public void testqueryByShopId() {
		long shopId = 1;
		Shop shop = shopDao.queryByShopId(shopId);
		System.out.println("areaId" + shop.getArea().getAreaId());
		System.out.println("areaName" + shop.getArea().getAreaName());

	}

	@Test
	@Ignore
	public void testInserShop() {
		Area area = new Area();
		PersonInfo owner = new PersonInfo();
		ShopCategory shopCategory = new ShopCategory();
		area.setAreaId(2);
		owner.setUserId(1L);
		shopCategory.setShopCategoryId(1L);
		Shop shop = new Shop();
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setOwner(owner);
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setPhone("18702694310");
		shop.setAdvice("审核中");
		shop.setShopName("测试的店铺");
		shop.setShopDesc("test");
		shop.setEnableStatus(1);
		shop.setPriority(1);
		shop.setShopImg("test");
		int effectNum = shopDao.insertShop(shop);
		assertEquals(1, effectNum);

	}
    @Ignore
	@Test
	public void testUpdateShop() throws FileNotFoundException {
		File shopImg = new File("F:/image/ee.png");
		//InputStream is = new FileInputStream(shopImg);


		Area area = new Area();
		PersonInfo owner = new PersonInfo();
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(1L);
		Shop shop = new Shop();
		shop.setShopImg(shopImg.getName());
		shop.setShopId(1L);
		shop.setPhone("13677917492");
		shop.setAdvice("已通过");
		shop.setShopName("更新后的店铺");
		shop.setShopDesc("test");
		//shop.setShopImg("test");
		int effectNum = shopDao.updateShop(shop);
		assertEquals(1, effectNum);

	}

}