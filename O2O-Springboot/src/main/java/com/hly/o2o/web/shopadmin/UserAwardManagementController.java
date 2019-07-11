package com.hly.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hly.o2o.dto.ShopAuthMapExecution;
import com.hly.o2o.dto.UserAccessToken;
import com.hly.o2o.dto.UserAwardMapExecution;
import com.hly.o2o.dto.WeChatInfo;
import com.hly.o2o.entity.*;
import com.hly.o2o.enums.UserProductMapStateEnum;
import com.hly.o2o.service.ShopAuthMapService;
import com.hly.o2o.service.UserAwardMapService;
import com.hly.o2o.service.WechatAuthService;
import com.hly.o2o.util.HttpServletRequestUtil;
import com.hly.o2o.util.wechat.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class UserAwardManagementController {
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    ShopAuthMapService shopAuthMapService;
    private Logger logger = LoggerFactory.getLogger(UserAwardManagementController.class);

    @RequestMapping(value = "/listawardshopmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserShopMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        // 从session中获取当前店铺的信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值判断
        if (pageIndex >= 0 && pageSize >= 0 && currentShop != null && currentShop.getShopId() != null) {
            UserAwardMap userAwardMapCondition = new UserAwardMap();
            // 获取奖品名称
            userAwardMapCondition.setShop(currentShop);
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                // 若传入顾客名，则按照顾客名模糊查询
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMapCondition.setAward(award);
            }
            // 分页获取该店铺下的顾客积分列表
            UserAwardMapExecution ud = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
            modelMap.put("userAwardMapList", ud.getUserAwardMapList());
            modelMap.put("count", ud.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty info");
        }
        return modelMap;
    }


    @RequestMapping(value = "/exchangeaward", method = RequestMethod.GET)
    @ResponseBody
    private String exchangeAward(HttpServletRequest request) {
        //获取微信用户信息
        WechatAuth weChatAuth = getOperatorInfo(request);
        if (null != weChatAuth) {
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
            if (!checkQRCodeVaild(wechatInfo)) {
                return "shop/operationfail";
            }
            Long userAwardId = wechatInfo.getUserAwardId();
            Long customerId = wechatInfo.getCustomerId();
            UserAwardMap userAwardMap = compactUserAwardMap4Add(customerId, userAwardId, operator);

            //空值检查
            if (userAwardId != null && customerId != null) {
                try {
                    //检查操作员是否有权限
                    if (!CheckShopAuth(operator.getUserId(), userAwardMap)) {
                        return "shop/operationfail";
                    }
                    UserAwardMapExecution upe = userAwardMapService.modifyUserrAwardMap(userAwardMap);
                    if (upe.getState() == UserProductMapStateEnum.SUCCESS.getState())
                        return "shop/operationsuccess";

                } catch (Exception e) {
                    return "shop/operationfail";
                }

            }
        }
        return "shop/operationfail";
    }


    private UserAwardMap compactUserAwardMap4Add(Long customerId, Long userAwardId, PersonInfo operator) {
        UserAwardMap userAwardMap = null;
        // 空值判断
        if (customerId != null && userAwardId != null && operator != null) {
            // 获取原有userAwardMap信息
            userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            userAwardMap.setUsedStatus(1);
            PersonInfo customer = new PersonInfo();
            customer.setUserId(customerId);
            userAwardMap.setUser(customer);
            userAwardMap.setOperator(operator);
        }
        return userAwardMap;
    }

    /**
     * 检查操作员权限
     *
     * @param userId
     * @param
     * @return
     */
    private boolean CheckShopAuth(Long userId, UserAwardMap userAwardMap) {
        ShopAuthMapExecution shopAuthMapExection = shopAuthMapService.getShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 0, 999);
        for (ShopAuthMap shopAuthMap : shopAuthMapExection.getShopAuthMapList()) {
            if (shopAuthMap.getEmployee().getUserId() == userId)
                return true;
        }
        return false;
    }

    private boolean checkQRCodeVaild(WeChatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getCreateTime() != null && wechatInfo.getUserAwardId() != null && wechatInfo.getCustomerId() != null) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - wechatInfo.getCreateTime() <= 600000)
                return true;
            else
                return false;
        }
        return false;
    }

    private WechatAuth getOperatorInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth auth = null;
        if (null != code) {
            UserAccessToken token = null;
            try {
                token = WeChatUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                logger.error("获取token失败");
            }
        }
        return auth;
    }
}
