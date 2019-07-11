package com.hly.o2o.service;

import com.hly.o2o.dao.ShopAuthMapDao;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.ShopAuthMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopAuthServiceTest {

    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Test
    public void testInsertShopAuthMap(){
        //创建店铺授权信息1
        ShopAuthMap shopAuthMap1=new ShopAuthMap();
        PersonInfo employee1=new PersonInfo();
        employee1.setUserId(1l);
        shopAuthMap1.setEmployee(employee1);
        Shop shop1=new Shop();
        shop1.setShopId(2l);
        shopAuthMap1.setShop(shop1);
        shopAuthMap1.setTitle("CEO");
        shopAuthMap1.setTitleFlag(1);
        shopAuthMap1.setEnableStatus(1);
        shopAuthMap1.setCreateTime(new Date());
        shopAuthMap1.setLastEditTime(new Date());
        int effectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap1);
        assertEquals(1,effectNum);


    }
}

