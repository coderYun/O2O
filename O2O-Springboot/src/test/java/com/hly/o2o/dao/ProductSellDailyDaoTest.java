package com.hly.o2o.dao;

import com.hly.o2o.entity.ProductSellDaily;
import com.hly.o2o.entity.Shop;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;


    /**
     * 测试添加功能
     */
    @Test
    public void testAInsertProductSellDaily() throws Exception{

        //创建商品销量统计
        int effectNum = productSellDailyDao.insertProductSellDaily();
        assertEquals(1,effectNum);

    }

    /**
     * 测试添加功能
     * @throws Exception
     */
    @Test
    public  void testBInsertDefaultProductSellDaily() throws Exception{
        int effectNum = productSellDailyDao.insertDefaultProductSellDaily();
        assertEquals(8,effectNum);

    }

    @Test
    public void testBQueryProductSellDaily() throws Exception{

        ProductSellDaily productSellDaily=new ProductSellDaily();
        Shop shop=new Shop();
        shop.setShopId(2l);
        productSellDaily.setShop(shop);
        List<ProductSellDaily> productSellDailiesList= productSellDailyDao.queryProductSellDailyList(productSellDaily, null, null);
        assertEquals(1,productSellDailiesList.size());
    }

}