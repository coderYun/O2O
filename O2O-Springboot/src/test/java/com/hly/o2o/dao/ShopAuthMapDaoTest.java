package com.hly.o2o.dao;

import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.ShopAuthMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopAuthMapDaoTest {
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    /**
     * 测试添加功能
     */
    @Test
    public void testAInsertShopAuthMap(){
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

        //创建店铺授权信息2
        ShopAuthMap shopAuthMap2=new ShopAuthMap();
        PersonInfo employee2=new PersonInfo();
        employee2.setUserId(2l);
        shopAuthMap2.setEmployee(employee2);
        Shop shop2=new Shop();
        shop2.setShopId(3l);
        shopAuthMap2.setShop(shop2);
        shopAuthMap2.setTitle("普通员工");
        shopAuthMap2.setTitleFlag(1);
        shopAuthMap2.setEnableStatus(0);
        shopAuthMap2.setCreateTime(new Date());
        shopAuthMap2.setLastEditTime(new Date());
        int effectNum1 = shopAuthMapDao.insertShopAuthMap(shopAuthMap2);
        assertEquals(1,effectNum1);

    }

    /**
     * 测试查询功能
     */
    @Test
    public void testBQueryShopAuth(){
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(2, 0, 2);
        assertEquals(1,shopAuthMapList.size());
        System.out.println(shopAuthMapList.get(0).getTitle());
        int effectNum = shopAuthMapDao.queryShopAuthCountByShopId(2l);
        assertEquals(1,effectNum);

        ShopAuthMap shopAuthMap = shopAuthMapDao.queryShopAuthMapById(shopAuthMapList.get(0).getShopAuthId());
        assertEquals("CTO",shopAuthMap.getTitle());


    }

    /**
     * 测试更新功能
     */
    @Test
    public void testCUpdateShopAuthMap(){
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(2, 0, 2);
        assertEquals("CEO",shopAuthMapList.get(0).getTitle());
        shopAuthMapList.get(0).setTitle("CTO");
        int effectNum = shopAuthMapDao.updateShopAuthMap(shopAuthMapList.get(0));
        assertEquals(1,effectNum);
    }

    /**
     * 测试删除功能
     */
    @Test
    public void testDdeleteAuthShopMap(){
        int effectNum = shopAuthMapDao.deleteShopAuthMap(2);
        assertEquals(1,effectNum);
    }

}
