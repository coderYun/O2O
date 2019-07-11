package com.hly.o2o.dao;

import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Product;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.UserProductMap;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserProductMaoDaoTest {
    @Autowired
    private UserProductMapDao userProductMapDao;


    /**
     * 测试添加功能
     */
    @Test
    public void testAInsertUserProductMapList() throws Exception{
        //创建用户商品映射信息1
        UserProductMap userProductMap1=new UserProductMap();
        PersonInfo customer=new PersonInfo();
        customer.setUserId(1l);
        userProductMap1.setUser(customer);
        userProductMap1.setOperator(customer);
        Product product=new Product();
        product.setProductId(54l);
        userProductMap1.setProduct(product);
        Shop shop=new Shop();
        shop.setShopId(2L);
        userProductMap1.setShop(shop);
        userProductMap1.setPoint(5);
        userProductMap1.setCreateTime(new Date());
        int effectNum1 = userProductMapDao.insertUserProductMap(userProductMap1);
        assertEquals(1,effectNum1);
        //创建用户商品信息2
        UserProductMap userProductMap2=new UserProductMap();
        PersonInfo customer1=new PersonInfo();
        customer1.setUserId(2l);
        userProductMap2.setUser(customer1);
        userProductMap2.setOperator(customer1);
        Product product1=new Product();
        product1.setProductId(56l);
        userProductMap2.setProduct(product1);
        Shop shop1=new Shop();
        shop1.setShopId(2L);
        userProductMap2.setShop(shop1);
        userProductMap2.setCreateTime(new Date());
        int effectNum2= userProductMapDao.insertUserProductMap(userProductMap2);
        assertEquals(1,effectNum2);

    }

    /**
     * 测试查询功能
     */
    @Test
    public void testBQueryUserProductMap(){
        UserProductMap userProductMap=new UserProductMap();
        PersonInfo user=new PersonInfo();
        user.setName("测试");
        userProductMap.setUser(user);
        List<UserProductMap> userProductMaps = userProductMapDao.queryUserProductMapList(userProductMap, 0, 2);
        assertEquals(1,userProductMaps.size());
        int i = userProductMapDao.queryUserProductMapCount(userProductMap);
        assertEquals(1,i);
        Shop shop=new Shop();
        shop.setShopId(2l);
        userProductMap.setShop(shop);
        List<UserProductMap> userProductMaps1 = userProductMapDao.queryUserProductMapList(userProductMap, 0, 2);
        assertEquals(1,userProductMaps1.size());



    }
}
