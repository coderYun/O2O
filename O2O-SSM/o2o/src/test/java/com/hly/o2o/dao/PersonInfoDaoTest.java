package com.hly.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.hly.o2o.BaseTest;
import com.hly.o2o.dao.PersonInfoDao;
import com.hly.o2o.entity.PersonInfo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonInfoDaoTest extends BaseTest {
	@Autowired
	private PersonInfoDao personInfoDao;
	@Test
	public void testAInsertPersonInfo() throws Exception {
		//新增用户信息
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName("我爱你");
		personInfo.setGender("女");
		personInfo.setUserType(1);
		personInfo.setCreateTime(new Date());
		personInfo.setLastEditTime(new Date());
		personInfo.setEnableStatus(1);
		int effectedNum = personInfoDao.insertPersonInfo(personInfo);
		assertEquals(1, effectedNum);
	}
	@Test
	public void testBqueryPersonInfoById(){
		//查询用户ID为1的名字
		long userId=1;
		PersonInfo person = personInfoDao.queryPersonInfoById(userId);
		System.out.println(person.getName());
	}
	

	/**@Test
	public void testBQueryPersonInfoList() throws Exception {
		PersonInfo personInfo = new PersonInfo();
		List<PersonInfo> personInfoList = personInfoDao.queryPersonInfoList(
				personInfo, 0, 10);
		assertEquals(3, personInfoList.size());
		int count = personInfoDao.queryPersonInfoCount(personInfo);
		assertEquals(3, count);
		personInfo.setName("test");
		personInfoList = personInfoDao.queryPersonInfoList(personInfo, 0, 3);
		assertEquals(2, personInfoList.size());
		count = personInfoDao.queryPersonInfoCount(personInfo);
		assertEquals(2, count);
		personInfo.setShopOwnerFlag(1);
		personInfoList = personInfoDao.queryPersonInfoList(personInfo, 0, 3);
		assertEquals(1, personInfoList.size());
		count = personInfoDao.queryPersonInfoCount(personInfo);
		assertEquals(1, count);

	}
*/
/*	@Test
	public void testDUpdatePersonInfo() {
		PersonInfo personInfo = new PersonInfo();
		long userId = 1;
		personInfo.setUserId(userId);
		personInfo.setGender("女");
		int effectedNum = personInfoDao.updatePersonInfo(personInfo);
		assertEquals(1, effectedNum);
	}

	@Test
	public void testEDeletePersonInfoByName() throws Exception {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName("我爱你");
		List<PersonInfo> personInfoList = personInfoDao.queryPersonInfoList(
				personInfo, 0, 1);
		int effectedNum = personInfoDao.deletePersonInfo(personInfoList.get(0)
				.getUserId());
		assertEquals(1, effectedNum);

	}
*/
}
