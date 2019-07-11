package com.hly.o2o.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hly.o2o.BaseTest;
import com.hly.o2o.dto.ImageHolder;
import com.hly.o2o.dto.ShopExecution;
import com.hly.o2o.entity.Area;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.ShopCategory;
import com.hly.o2o.enums.ShopStateEnum;
import com.hly.o2o.exceptions.ShopOperationException;
import com.hly.o2o.service.ShopService;

public class ShopServiceTest extends BaseTest {
	@Autowired
	private ShopService shopService;

	@Test
	public void testQueryShopListAndCount() {
		Shop shopCodition = new Shop();
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(2L);
		shopCodition.setShopCategory(sc);
		ShopExecution shopExecution = shopService.getShopList(shopCodition, 2, 2);
		System.out.println("店铺列表数=====" + shopExecution.getShopList().size());
		System.out.println("店铺总数数=====" + shopExecution.getCount());
	}

	@Ignore
	@Test
	public void testModifyShop() throws FileNotFoundException, ShopOperationException {
		Shop shop = new Shop();
		shop.setShopId(23L);
		shop.setShopName("修改店铺后的名称1");
		File shopImg = new File("F:/image/ee.png");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder("ee.png", is);
		ShopExecution modifyShop = shopService.modifyShop(shop, imageHolder);
		System.out.println("新的图片地址==" + modifyShop.getShop().getShopImg());

	}

	@Ignore
	@Test
	public void addShopTest() throws FileNotFoundException {
		Shop shop = new Shop();
		ShopCategory shopCategory = new ShopCategory();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		shopCategory.setShopCategoryId(1L);
		area.setAreaId(2);
		owner.setUserId(1L);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setOwner(owner);
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setShopName("测试的店铺5");
		shop.setShopDesc("test4");
		shop.setShopAddr("test4");
		shop.setAdvice("审核中");
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		File shopImg = new File("F:/image/ee.png");
		InputStream is = new FileInputStream(shopImg);
		ImageHolder imageHolder = new ImageHolder(shopImg.getName(), is);
		ShopExecution shopExecution = shopService.addShop(shop, imageHolder);
		assertEquals(ShopStateEnum.CHECK.getState(), shopExecution.getState());
	}

}
