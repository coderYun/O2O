package com.hly.o2o.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hly.o2o.BaseTest;
import com.hly.o2o.dao.HeadLineDao;
import com.hly.o2o.entity.HeadLine;

public class HeadLineDaoTest extends BaseTest{
	@Autowired
	private HeadLineDao headLineDao;
	@Test
	public void testQueryHeadLine(){
		List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
		assertEquals(headLineList.size(),0);
	}

}
