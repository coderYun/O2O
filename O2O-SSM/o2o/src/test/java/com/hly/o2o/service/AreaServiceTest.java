package com.hly.o2o.service;
import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hly.o2o.BaseTest;
import com.hly.o2o.entity.Area;
import com.hly.o2o.service.AreaService;
import com.hly.o2o.service.CacheService;

public class AreaServiceTest extends BaseTest {
	@Autowired
	private AreaService areaService;
	@Autowired
	private CacheService cacheService;
	@Test
	public void testGetAreaList(){
		List<Area> areaList = areaService.getAreaList();
		assertEquals("东理北门",areaList.get(0).getAreaName());
		cacheService.removeFromCache(areaService.AREALISTKEY);
		areaList=areaService.getAreaList();
		
	}

}
