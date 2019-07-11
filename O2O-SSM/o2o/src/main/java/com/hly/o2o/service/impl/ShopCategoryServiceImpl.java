package com.hly.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hly.o2o.cache.JedisUtil;
import com.hly.o2o.dao.ShopCategoryDao;
import com.hly.o2o.entity.HeadLine;
import com.hly.o2o.entity.ShopCategory;
import com.hly.o2o.exceptions.HeadLineOperationException;
import com.hly.o2o.exceptions.ShopCategoryOperationException;
import com.hly.o2o.service.ShopCategoryService;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	@Autowired
	private JedisUtil.Strings jedisStrings;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	private static Logger log = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);
	@Override
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
		// 定义key的前缀
		String key = SCLISTKEY;
		// 定义接收结果的变量
		List<ShopCategory> shopCategoryList = null;
		// 定义json数据类型转换操作类
		ObjectMapper mapper = new ObjectMapper();
		// 拼接出redis的key
		if (shopCategoryCondition == null) {
			// 若是shopCategoryCondition为空,则列出首页大类，即parentId为空
			key = key + "_" + "allfirstlevel";
		} else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null
				&& shopCategoryCondition.getParent().getShopCategoryId() != null) {
			//若是parentId不为空，则列出下面所以类别来
			key=key+"_parent"+shopCategoryCondition.getParent().getShopCategoryId();

		}else if(shopCategoryCondition!=null){
			
			//列出所有子列别,不管是哪个都列出来
			key=key+"_"+"allsecondlevle";
		}
		//判断key是否存在
		// 判断key是否存在
		if (!jedisKeys.exists(key)) {
			// 如果不存在，从数据库里取出相应的数据
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
			// 将想关的集合类型转化为String,并且存储到redis里面去
			String jsonString;
			try {
				jsonString = mapper.writeValueAsString(shopCategoryList);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
			jedisStrings.set(key, jsonString);
		} else {// 如果redis中有就直接去Redis中拿数据
			String jsonString = jedisStrings.get(key);
			//将得到的String类型转化为相对应的json List类类型返回给前台
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			try{
			shopCategoryList=mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
				
			} catch (JsonMappingException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			
			} catch (IOException e) {
				e.printStackTrace();;
				log.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}

		}

		return shopCategoryList;
	}

}
