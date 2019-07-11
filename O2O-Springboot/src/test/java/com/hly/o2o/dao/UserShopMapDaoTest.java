package com.hly.o2o.dao;

import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.UserShopMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserShopMapDaoTest {

    @Autowired
    private UserShopMapDao userShopMapDao;

    /**
     * 测试插入功能
     */
    @Test
    public void testAInsertUserShopMapTest(){
        //创建用户店铺统计积分信息1
        UserShopMap userShopMap1=new UserShopMap();
        Shop shop=new Shop();
        shop.setShopId(2l);
        PersonInfo customer=new PersonInfo();
        customer.setUserId(1l);
        userShopMap1.setShop(shop);
        userShopMap1.setUser(customer);
        userShopMap1.setCreateTime(new Date());
        userShopMap1.setPoint(1);
        int efffectNum = userShopMapDao.insertUserShopMap(userShopMap1);
        assertEquals(1,efffectNum);
        //创建用户店铺统计积分信息2
        UserShopMap userShopMap2=new UserShopMap();
        Shop shop2=new Shop();
        shop2.setShopId(3l);
        userShopMap2.setPoint(1);
        userShopMap2.setShop(shop2);
        PersonInfo customer2=new PersonInfo();
        customer2.setUserId(2l);
        userShopMap2.setUser(customer2);
        userShopMap2.setCreateTime(new Date());
        int effectNum2 = userShopMapDao.insertUserShopMap(userShopMap2);
        assertEquals(1,effectNum2);
    }

    @Test
    public void testBQueryUserShopMap(){
        UserShopMap userShopMap=new UserShopMap();
        int count = userShopMapDao.queryUserShopMapCount(userShopMap);
        assertEquals(2,count);

        //按店铺去查询
        Shop shop=new Shop();
        shop.setShopId(2l);
        userShopMap.setShop(shop);
        List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 2);
        assertEquals(1, userShopMapList.size());

        //按店铺ID和用户ID去查询
        UserShopMap userShopMap1 = userShopMapDao.queryUserShopMap(1, 2);
        assertEquals("测试",userShopMap1.getUser().getName());

    }

    @Test
    public void testCUpdateUserShopMap(){
        UserShopMap userShopMap1 = userShopMapDao.queryUserShopMap(1, 2);
        assertTrue("积分不一致",1==userShopMap1.getPoint());
        userShopMap1.setPoint(2);
        int i = userShopMapDao.updateUserShopMapPoint(userShopMap1);
        assertEquals(1,i);
    }


}
