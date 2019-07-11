package com.hly.o2o.service.impl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hly.o2o.cache.JedisUtil;
import com.hly.o2o.dao.HeadLineDao;
import com.hly.o2o.entity.HeadLine;
import com.hly.o2o.exceptions.AreaOperationException;
import com.hly.o2o.exceptions.HeadLineOperationException;
import com.hly.o2o.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService {
	@Autowired
	private HeadLineDao headLineDao;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	@Autowired
	private JedisUtil.Keys jedisKeys;

	private static Logger log = LoggerFactory.getLogger(HeadLineServiceImpl.class);

	@Override
	@Transactional
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {

		// 定义redis Key的前缀
		String key = HLLISTKEY;
		// 定义接收返回数据的对象
		List<HeadLine> headLineList = null;
		// 定义json数据类型转换操作类
		ObjectMapper mapper = new ObjectMapper();
		// 拼接出redisKey
		if (headLineCondition != null && headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();

		}
		// 判断key是否存在
		if (!jedisKeys.exists(key)) {
			// 如果不存在，从数据库里取出相应的数据
			headLineList = headLineDao.queryHeadLine(headLineCondition);
			// 将想关的集合类型转化为String,并且存储到redis里面去
			String jsonString;
			try {
				jsonString = mapper.writeValueAsString(headLineList);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
			jedisStrings.set(key, jsonString);
		} else {// 如果redis中有就直接去Redis中拿数据
			String jsonString = jedisStrings.get(key);
			//将得到的String类型转化为相对应的json List类类型返回给前台
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
			try{
			headLineList=mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
				
			} catch (JsonMappingException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			
			} catch (IOException e) {
				e.printStackTrace();;
				log.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}

		}
		return headLineList;

		
	}

}
