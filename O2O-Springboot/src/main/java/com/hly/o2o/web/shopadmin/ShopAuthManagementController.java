package com.hly.o2o.web.shopadmin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.hly.o2o.dto.ShopAuthMapExecution;
import com.hly.o2o.dto.UserAccessToken;
import com.hly.o2o.dto.WeChatInfo;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.ShopAuthMap;
import com.hly.o2o.entity.WechatAuth;
import com.hly.o2o.enums.ShopAuthMapStateEnum;
import com.hly.o2o.service.PersonInfoService;
import com.hly.o2o.service.ShopAuthMapService;
import com.hly.o2o.service.WechatAuthService;
import com.hly.o2o.util.CodeUtil;
import com.hly.o2o.util.HttpServletRequestUtil;
import com.hly.o2o.util.ShortNetAddressUtil;
import com.hly.o2o.util.wechat.WeChatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/shopadmin")
public class ShopAuthManagementController {

    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private PersonInfoService personInfoService;


    @RequestMapping(value = "/listshopauthmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopAuthMapsByShop(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<String, Object>();
        //取出分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从session中获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
            //分页取出店铺授权信息表
            ShopAuthMapExecution se = shopAuthMapService.getShopAuthMapByShopId(currentShop.getShopId(), pageIndex, pageSize);
            modelMap.put("shopAuthMapList", se.getShopAuthMapList());
            modelMap.put("count", se.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "the pageIndex or pageSize or shopId is null");
        }
        return modelMap;
    }


    @RequestMapping(value = "/getshopauthmapbyid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopAuthMapById(@RequestParam Long shopAuthId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //非空判断
        if (shopAuthId != null && shopAuthId > -1) {
            //根据前台传入的shopAuthId找到相对应的店铺授权信息
            ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
            if (shopAuthMap != null) {
                modelMap.put("success", true);
                modelMap.put("shopAuthMap", shopAuthMap);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "不存在此人!");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopAuthId");
        }
        return modelMap;
    }


    @ResponseBody
    @RequestMapping(value = "/modifyshopauthmap", method = RequestMethod.POST)
    private Map<String, Object> modifyShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //是授权编辑时调用还是删除/恢复授权操作时调用
        //若是前者则需要进行验证码校验，若为后者则不需要进行验证码校验
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        //验证码校验
        if (!statusChange && (!CodeUtil.checkVerifyCode(request))) {
            modelMap.put("success", "false");
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        ObjectMapper mapper = new ObjectMapper();
        ShopAuthMap shopAuthMap = null;
        try {
            //将前台传过来的json字符串转化为shopAuthMap实例
            shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        //空值判断
        if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
            try {
                //判断是否】被操作的对象是否为店家本身，店家本身不支持修改(因为他已经是最高权限了)
                if (!checkPermission(shopAuthMap.getShopAuthId())) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "不能修改(店家已经是最高权限)");
                }
                ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;

            }

        } else {
            modelMap.put("success", true);
            modelMap.put("errMsg", "请输入要授权的信息");
        }
        return modelMap;
    }

    private boolean checkPermission(Long shopAuthId) {

        ShopAuthMap grantPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
        if (grantPerson.getTitleFlag() == 0) {
            //若titleFlag为0,则表示店家本身，不能操作
            return false;
        }
        return true;
    }

    //获取微信用户信息api的前缀
    private static String urlPrefix;
    //获取微信用户信息api的中间部分
    private static String urlMiddle;
    //微信用户信息api的后缀部分
    private static String urlSuffix;

    //微信回传的响应的添加授权信息url
    private static String authUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ShopAuthManagementController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ShopAuthManagementController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ShopAuthManagementController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        ShopAuthManagementController.authUrl = authUrl;
    }

    /**
     * 生成带有URL的二维码
     * 微信扫一扫就能链接到对应的URL中
     *
     * @param request
     * @param response
     */

    @RequestMapping(value = "/generateqrcode4shopauth", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {
        // 从session中获取当前shop的信息
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        if (shop != null && shop.getShopId() != null) {
            // 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
            long timeStamp = System.currentTimeMillis();
            // 将店铺id和timestamp传入content，赋值到state中,
            // 微信获取到这些信息会回传到授权信息的添加方法中
            // 加上aaa是为了在添加信息的方法中替换这些信息使用
            String content = "{aaashopIdaaa:" + shop.getShopId() + ",aaacreateTimeaaa:" + timeStamp + "}";
            try {
                // 将content的信息先进行base64编码以避免特殊字符造成干扰，之后拼接目标URL
                String longUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
                // 将目标URL转换成短的URL
                String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
                // 调用二维码生成工具类方法，传入短的URL，生成二维码
                BitMatrix QRCodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
                // 将二维码以图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(QRCodeImg, "png", response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据微信回传回来的信息添加店铺的授权信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/addshopauthmap", method = RequestMethod.GET)
    private String addShopAuthMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //从request中获取微信用户的信息
        WechatAuth auth = getEmployyeeInfo(request);

        if (auth != null) {
            //根据userId获取用户信息
            PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            //将用户信息添加进user里面
            request.getSession().setAttribute("user", user);
            //解析微信回传过来的自定义参数state,因为之前已经进行了编码，所以这里需要解码
            String qrCodeInfo = new String(URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
            ObjectMapper mapper = new ObjectMapper();
            WeChatInfo weChatInfo = null;
            try {
                //转换成WechatInfo实体类
                weChatInfo = mapper.readValue(qrCodeInfo.replace("aaa", "\""), WeChatInfo.class);
            } catch (Exception e) {
                return "shop/operationfail";
            }
            //校验二维码是否过期
            if (!checkQRCodeInfo(weChatInfo)) {
                return "shop/operationfail";
            }
            // 去重校验
            // 获取该店铺下所有的授权信息
            // 避免重复扫描，重复添加数据库
            ShopAuthMapExecution allMapList = shopAuthMapService.getShopAuthMapByShopId(weChatInfo.getShopId(), 1, 999);
            List<ShopAuthMap> shopAuthList = allMapList.getShopAuthMapList();
            for (ShopAuthMap sm : shopAuthList) {
                if (sm.getEmployee().getUserId() == user.getUserId())
                    return "shop/operationfail";
            }
            try {
                //根据微信获取到的内容，添加店铺授权信息
                ShopAuthMap shopAuthMap = new ShopAuthMap();
                Shop shop = new Shop();
                shop.setShopId(weChatInfo.getShopId());
                shopAuthMap.setShop(shop);
                shopAuthMap.setEmployee(user);
                shopAuthMap.setTitle("员工");
                shopAuthMap.setTitleFlag(1);
                ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    return "shop/operationsuccess";
                } else {
                    return "shop/operationfail";
                }
            } catch (RuntimeException e) {
                return "shop/operationfail";
            }
        }
        return "shop/operationfail";

    }

    /**
     * 根据二维码携带的createTime判断是否超时（10分钟），超时则过期
     *
     * @param wechatInfo
     * @return
     */
    private boolean checkQRCodeInfo(WeChatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getCreateTime() != null) {
            long nowTime = System.currentTimeMillis();
            if ((nowTime - wechatInfo.getCreateTime()) <= 600000) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 根据微信回传的信息获取用户的信息
     *
     * @param request
     */
    private WechatAuth getEmployyeeInfo(HttpServletRequest request) {
        //先获取微信回传的code
        String code = request.getParameter("code");
        WechatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            try {
                token = WeChatUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return auth;
    }


}
