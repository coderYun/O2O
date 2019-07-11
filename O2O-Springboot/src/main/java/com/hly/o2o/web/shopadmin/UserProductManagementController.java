package com.hly.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hly.o2o.dto.*;
import com.hly.o2o.entity.*;
import com.hly.o2o.enums.UserProductMapStateEnum;
import com.hly.o2o.service.*;
import com.hly.o2o.util.HttpServletRequestUtil;
import com.hly.o2o.util.wechat.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/shopadmin")
public class UserProductManagementController {
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private ProductService productService;

    private Logger logger=LoggerFactory.getLogger(UserProductManagementController.class);

    @RequestMapping(value = "/listuserproductmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserProductMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取当前店铺的信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值校验
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            //添加查询条件
            UserProductMap userProductMapCondition = new UserProductMap();
            userProductMapCondition.setShop(currentShop);
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                //若前端想按照商品模糊查询，则传入productName
                Product product = new Product();
                product.setProductName(productName);
                userProductMapCondition.setProduct(product);
            }
            //根据传入的条件获取商品销售
            UserProductMapExecution se = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex, pageSize);
            modelMap.put("userProductMapList", se.getUserProductMapList());
            modelMap.put("count", se.getCount());
            modelMap.put("success", true);

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId or pageIndex or pageSize");
        }
        return modelMap;
    }

    /**
     * 统计出当天店铺所有商品的日销售量
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductSellDailyInfoByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取当前店铺
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值校验，主要确保shopId不为空
        if ((currentShop != null) && (currentShop.getShopId() != null)) {
            //添加查询条件
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currentShop);
            Calendar calendar = Calendar.getInstance();
            //获取昨天的日期
            calendar.add(Calendar.DATE, -1);
            Date endTime = calendar.getTime();
            //获取七天前的日期
            calendar.add(Calendar.DATE, -6);
            Date beginTime = calendar.getTime();
            //根据传入的条件查询该店铺各个商品的日销售量
            List<ProductSellDaily> productSellDailyList = productSellDailyService.listProductSellDaily(productSellDailyCondition, beginTime, endTime);
            //指定日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //商品名列表，保证唯一性
            HashSet<String> legendData = new HashSet<String>();
            //X轴数据
            HashSet<String> xData = new HashSet<String>();
            //定义series
            List<EchartSeries> series = new ArrayList<EchartSeries>();
            //日销量列表
            List<Integer> totalList = new ArrayList<Integer>();
            //当前商品名，默认为空
            String currentProductName = "";
            for (int i = 0; i < productSellDailyList.size(); i++) {
                ProductSellDaily productSellDaily = productSellDailyList.get(i);
                //自动去重(hastSet不允许存储重复元素)
                legendData.add(productSellDaily.getProduct().getProductName());
                xData.add(sdf.format(productSellDaily.getCreateTime()));
                //如果当前的商品名不等于获取的商品名，则表示已经遍历完了这个商品并且currentProductName不能为空
                //则遍历下一个商品的日销售信息，把前一个的商品的日销售信息放到series中
                //包过商品名已经统计的日期和销售量
                if (!currentProductName.equals(productSellDaily.getProduct().getProductName()) && (!currentProductName.isEmpty())) {
                    EchartSeries echartSeries = new EchartSeries();
                    echartSeries.setName(currentProductName);
                    echartSeries.setData(totalList.subList(0, totalList.size()));
                    series.add(echartSeries);
                    //重置totalList
                    totalList = new ArrayList<Integer>();
                    //变换下currentProductId为当前的productId
                    currentProductName = productSellDaily.getProduct().getProductName();
                    //继续添加新的值
                    totalList.add(productSellDaily.getTotal());

                } else {
                    //如果还是当前的productId则继续添加新值
                    totalList.add(productSellDaily.getTotal());
                    currentProductName = productSellDaily.getProduct().getProductName();
                }
                //队列末尾，最后一个商品的销量信息也要加上
                if (i == totalList.size() - 1) {
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                }
            }
            modelMap.put("series", series);
            modelMap.put("legendData", legendData);
            //拼接出xAxis
            List<EchartXAxis> xAxis = new ArrayList<EchartXAxis>();
            EchartXAxis exa = new EchartXAxis();
            exa.setData(xData);
            xAxis.add(exa);
            modelMap.put("xAxis", xAxis);
            modelMap.put("success", "true");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;

    }

    @RequestMapping(value = "/adduserproductmap", method = RequestMethod.GET)
    @ResponseBody
    private String addUserProductMap(HttpServletRequest request){
        //获取微信用户信息
        WechatAuth weChatAuth = getOperatorInfo(request);
        if(null != weChatAuth) {
            PersonInfo operator = weChatAuth.getPersonInfo();
            request.getSession().setAttribute("user", operator);
            //解析微信回传的自定义参数，将content进行解码
            String qrCodeinfo = null;
            try {
                qrCodeinfo = new String(
                        URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return "shop/operationfail";
            }
            ObjectMapper mapper = new ObjectMapper();
            WeChatInfo wechatInfo = null;  //封装二维码操作需要的各类ID（例如shopId customerId ...）
            try {
                wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WeChatInfo.class);
            } catch (IOException e) {
                return "shop/operationfail";
            }
            //检验二维码是否过期
            if(!checkQRCodeVaild(wechatInfo)) {
                return "shop/operationfail";
            }
            Long productId = wechatInfo.getProductId();
            Long customerId = wechatInfo.getCustomerId();
            UserProductMap userProductMap = compactUserProductMap4Add(customerId, productId, weChatAuth.getPersonInfo());

            //空值检查
            if(productId != null && customerId != null) {
                try {
                    //检查操作员是否有权限
                    if(!CheckShopAuth(operator.getUserId(), userProductMap)) {
                        return "shop/operationfail";
                    }
                    UserProductMapExecution upe = userProductMapService.addUserProductMap(userProductMap);
                    if(upe.getState() == UserProductMapStateEnum.SUCCESS.getState())
                        return "shop/operationsuccess";

                }catch (Exception e) {
                    return "shop/operationfail";
                }

            }
        }
        return "shop/operationfail";
    }

    /**
     * 检查操作员权限
     * @param userId
     * @param userProductMap
     * @return
     */
    private boolean CheckShopAuth(Long userId, UserProductMap userProductMap) {
        //获取该店铺的所有授权信息
        ShopAuthMapExecution shopAuthMapExection = shopAuthMapService.getShopAuthMapByShopId(userProductMap.getShop().getShopId(), 0, 999);
        for(ShopAuthMap shopAuthMap : shopAuthMapExection.getShopAuthMapList()) {
            //看看是否给过这个人操作权限
            if(shopAuthMap.getEmployee().getUserId() == userId)
                return true;
        }
        return false;
    }

    /**
     * 根据传入的customerId, productId以及操作员信息组建用户消费记录
     *
     * @param customerId
     * @param productId
     * @param operator
     * @return
     */
    private UserProductMap compactUserProductMap4Add(Long customerId, Long productId, PersonInfo operator) {
        UserProductMap userProductMap = null;
        if (customerId != null && productId != null) {
            userProductMap = new UserProductMap();
            PersonInfo customer = new PersonInfo();
            customer.setUserId(customerId);
            // 主要为了获取商品积分
            Product product = productService.getProductById(productId);
            userProductMap.setProduct(product);
            userProductMap.setShop(product.getShop());
            userProductMap.setUser(customer);
            userProductMap.setPoint(product.getPoint());
            userProductMap.setCreateTime(new Date());
            userProductMap.setOperator(operator);
        }
        return userProductMap;
    }

    private boolean checkQRCodeVaild(WeChatInfo wechatInfo) {
        if(wechatInfo != null && wechatInfo.getCreateTime() != null && wechatInfo.getProductId() != null && wechatInfo.getCustomerId() != null) {
            long nowTime = System.currentTimeMillis();
            if(nowTime - wechatInfo.getCreateTime() <= 600000)
                return true;
            else
                return false;
        }
        return false;
    }

    private WechatAuth getOperatorInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth auth = null;
        if(null != code) {
            UserAccessToken token = null;
            try {
                token = WeChatUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            }catch (IOException e) {
                logger.error("获取token失败");
            }
        }
        return auth;
    }


}
