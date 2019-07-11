package com.hly.o2o.dao;

import com.hly.o2o.entity.Award;
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
public class AwardDaoTest  {
    @Autowired
    private AwardDao awardDao;

    /**
     * 添加奖品测试
     */
    @Test
    public void testAInsertAward(){
        long shopId=2;
        Award award=new Award();
        award.setAwardName("奖品1");
        award.setAwardImg("f://image/award");
        award.setAwardDesc("奖品一描述");
        award.setCreateTime(new Date());
        award.setLastEditTime(new Date());
        award.setPoint(5);
        award.setPriority(1);
        award.setEnableStatus(1);
        award.setShopId(shopId);
        int effectedNum = awardDao.insertAward(award);
        assertEquals(1,effectedNum);

        Award award2=new Award();
        award2.setAwardName("奖品2");
        award2.setAwardImg("f://image/award");
        award2.setAwardDesc("奖品2描述");
        award2.setCreateTime(new Date());
        award2.setLastEditTime(new Date());
        award2.setPoint(6);
        award2.setPriority(2);
        award2.setEnableStatus(1);
        award2.setShopId(shopId);
        int effectedNum2 = awardDao.insertAward(award);
        assertEquals(1,effectedNum2);


    }
    @Test
    public  void testBQueryAwardList(){
        Award award=new Award();
        List<Award> awardList = awardDao.queryAwardList(award, 0, 3);
        assertEquals(3,awardList.size());

        int awardCount = awardDao.queryAwardCount(award);
        assertEquals(4,awardCount);

        award.setAwardName("奖品");
        int i = awardDao.queryAwardCount(award);
        assertEquals(3,i);

    }

    @Test
    public void testCQueryAwardById(){
        Award award=new Award();
        award.setAwardName("奖品1");
        List<Award> awards = awardDao.queryAwardList(award, 0, 1);
        assertEquals(1,awards.size());
        Award award1 = awardDao.queryAwardByAwardId(awards.get(0).getAwardId());
        assertEquals("奖品1",award1.getAwardName());
    }

    @Test
    public void testCUpdateAward(){
        Award award=new Award();
        award.setAwardName("奖品1");
        List<Award> awards = awardDao.queryAwardList(award, 0, 1);
        awards.get(0).setAwardName("第一个奖品1");
        int effectNum = awardDao.updateAward(awards.get(0));
        assertEquals(1,effectNum);

        Award award1 = awardDao.queryAwardByAwardId(awards.get(0).getAwardId());
        assertEquals("第一个奖品1",award1.getAwardName());
    }

    @Test
    public void TestDDeleteAward(){
        Award award=new Award();
        award.setAwardName("第一个奖品1");
        List<Award> awardList = awardDao.queryAwardList(award, 0, 1);
        assertEquals(1,awardList.size());
        for(Award award1:awardList){
            int effectNum = awardDao.deleteAward(award1.getAwardId(), award1.getShopId());
            assertEquals(1,effectNum);
        }
    }


}
