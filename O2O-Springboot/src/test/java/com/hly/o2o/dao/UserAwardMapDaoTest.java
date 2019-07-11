package com.hly.o2o.dao;

import com.hly.o2o.entity.Award;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.UserAwardMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAwardMapDaoTest {
    @Autowired
    private UserAwardMapDao userAwardMapDao;

    /**
     * 奖品添加功能的测试
     */
    @Test
    public void testAInsertUserAwardMap() {
        UserAwardMap userAwardMap = new UserAwardMap();
        PersonInfo customer = new PersonInfo();
        customer.setUserId(1l);
        userAwardMap.setCreateTime(new Date());
        userAwardMap.setUser(customer);
        userAwardMap.setOperator(customer);
        Award award = new Award();
        award.setAwardId(1l);
        userAwardMap.setAward(award);
        Shop shop = new Shop();
        shop.setShopId(2l);
        userAwardMap.setShop(shop);
        userAwardMap.setUsedStatus(1);
        userAwardMap.setPoint(1);
        int effectNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
        assertEquals(1, effectNum);
        //创建用户奖品信息实体2
        UserAwardMap userAwardMap2 = new UserAwardMap();
        PersonInfo customer2 = new PersonInfo();
        customer2.setUserId(1l);
        userAwardMap2.setCreateTime(new Date());
        userAwardMap2.setUser(customer2);
        userAwardMap2.setOperator(customer2);
        Award award2 = new Award();
        award2.setAwardId(1l);
        userAwardMap2.setAward(award2);
        userAwardMap2.setShop(shop);
        userAwardMap2.setUsedStatus(0);
        userAwardMap2.setPoint(1);
        int effectNum2 = userAwardMapDao.insertUserAwardMap(userAwardMap2);
        assertEquals(1, effectNum2);
    }

    /**
     * 测试查询功能
     */
        @Test
        public void testBQueryUserAwardMapList(){
            UserAwardMap userAwardMap=new UserAwardMap();
            List<UserAwardMap> awardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
            assertEquals(2,awardMapList.size());
            int count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
            assertEquals(2,count);
            //按用户名模糊查询
            PersonInfo customer=new PersonInfo();
            customer.setName("测试");
            userAwardMap.setUser(customer);
            List<UserAwardMap> mapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
            assertEquals(2,mapList.size());
            int count1 = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
            assertEquals(2,count1);
            //测试通过id查询
            UserAwardMap userAwardMap1 = userAwardMapDao.queryUserAwardMapById(mapList.get(0).getUserAwardId());
            assertEquals("红茶奖品",userAwardMap1.getAward().getAwardName());


        }
    /**
     * 测试更新功能
     */
    @Test
    public void testCUpdateUserAwardMap(){
        UserAwardMap userAwardMap=new UserAwardMap();
        PersonInfo customer=new PersonInfo();
        customer.setName("测试");
        userAwardMap.setUser(customer);
        List<UserAwardMap> awardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 1);
        assertTrue("error 积分不一致!",0==awardMapList.get(0).getUsedStatus());
        awardMapList.get(0).setUsedStatus(1);
        int effectNum = userAwardMapDao.updateUserAwardMap(userAwardMap);
        assertEquals(1,effectNum);


    }





}
