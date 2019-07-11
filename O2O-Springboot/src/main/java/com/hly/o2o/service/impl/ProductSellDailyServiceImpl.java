package com.hly.o2o.service.impl;

import com.hly.o2o.dao.ProductSellDailyDao;
import com.hly.o2o.entity.ProductSellDaily;
import com.hly.o2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {
    private static final Logger log = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Override
    public void dailyCalculate() {
        log.info("quartz Running!");
        //统计每个店铺下的商品的日销售量
        productSellDailyDao.insertProductSellDaily();
        //统计每个店铺下其余商品销量为0的(将它们销量全部置为0，为了迎合echarts的数据请求)
        productSellDailyDao.insertDefaultProductSellDaily();
        //  System.out.println("quaryTz跑起来了....");

    }

    @Override
    public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime) {
        return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
    }

}
