package com.hly.o2o.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.hly.o2o.dto.ImageHolder;
import com.hly.o2o.dto.UserAwardMapExecution;
import com.hly.o2o.entity.Award;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.UserAwardMap;
import com.hly.o2o.enums.UserAwardMapStateEnum;
import com.hly.o2o.service.AwardService;
import com.hly.o2o.service.PersonInfoService;
import com.hly.o2o.service.UserAwardMapService;
import com.hly.o2o.util.CodeUtil;
import com.hly.o2o.util.HttpServletRequestUtil;
import com.hly.o2o.util.ShortNetAddressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MyAwardController {
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private PersonInfoService userInfoService;
    @Autowired
    private AwardService awardService;
    private Logger logger= LoggerFactory.getLogger(MyAwardController.class);

    @RequestMapping(value = "/adduserawardmap", method = RequestMethod.POST)
    private Map<String, Object> addUserAwardMap(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        long awardId = HttpServletRequestUtil.getlong(request, "awardId");
        UserAwardMap userAwardMap = compactUserAwardCondition(user, awardId);
        if (userAwardMap != null) {
            try {
                UserAwardMapExecution sd = userAwardMapService.addUserAwardMap(userAwardMap);
                if (sd.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", sd.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请选择领取的奖品");
        }
        return modelMap;
    }

    /**
     * 封装商品查询条件到Product实例中
     *
     * @param user
     * @param awardId
     */
    private UserAwardMap compactUserAwardCondition(PersonInfo user, long awardId) {
        UserAwardMap userAwardMap = new UserAwardMap();
        userAwardMap.setUser(user);
        userAwardMap.setUserAwardId(awardId);
        return userAwardMap;
    }

    /**
     * 处理压缩图和详情图
     *
     * @param request
     * @param thumbnail
     * @return
     * @throws IOException
     */
    private ImageHolder handleImage(javax.servlet.http.HttpServletRequest request, ImageHolder thumbnail)
            throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 取出缩略图并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if (thumbnailFile != null) {
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }
        return thumbnail;
    }

    @RequestMapping(value = "/listuserawardmapsbycustomer", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserAwardMapsByCustomer(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从session中获取用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
            //用户查询条件
            UserAwardMap userAwardMapCondition = new UserAwardMap();
            userAwardMapCondition.setUser(user);
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if(null != awardName && "".equals(awardName)) {
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMapCondition.setAward(award);
            }
            UserAwardMapExecution userAwardMapExecution = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
            modelMap.put("userAwardMapList", userAwardMapExecution.getUserAwardMapList());
            modelMap.put("count", userAwardMapExecution.getCount());
            modelMap.put("success", true);
        }else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or userId");
        }
        return modelMap;
    }

    /**
     * 根据顾客奖品映射Id获取单条顾客奖品的映射信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getawardbyuserawardid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getAwardbyId(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 获取前端传递过来的userAwardId
        long userAwardId = HttpServletRequestUtil.getlong(request, "userAwardId");
        // 空值判断
        if (userAwardId > -1) {
            // 根据Id获取顾客奖品的映射信息，进而获取奖品Id
            UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            // 根据奖品Id获取奖品信息
            Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
            // 将奖品信息和领取状态返回给前端
            modelMap.put("award", award);
            modelMap.put("usedStatus", userAwardMap.getUsedStatus());
            modelMap.put("userAwardMap", userAwardMap);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "获取信息失败");
        }
        return modelMap;
    }

    //微信获取用户信息的api前缀
    private static  String urlPrefix;
    //微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    //微信回传响应添加用户奖品映射信息的url
    private static String exchangeUrl;

    @Value("${wechat.prefix}")
    public  void setUrlPrefix(String urlPrefix) {
        MyAwardController.urlPrefix = urlPrefix;
    }
    @Value("${wechat.middle}")
    public  void setUrlMiddle(String urlMiddle) {
        MyAwardController.urlMiddle = urlMiddle;
    }
    @Value("${wechat.suffix}")
    public  void setUrlSuffix(String urlSuffix) {
        MyAwardController.urlSuffix = urlSuffix;
    }

    public  void setExchangeUrl(String exchangeUrl) {
        MyAwardController.exchangeUrl = exchangeUrl;
    }

    /**
     * 生成二维码图片流并返回给前端
     * @param request
     * @param response
     */
    @RequestMapping(value = "/generateqrcode4award", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
        long userAwardId = HttpServletRequestUtil.getlong(request, "userAwardId");
        UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
        //确保用户处于登录状态
        PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
        if(userAwardId !=  -1 && user != null && user.getUserId() != null && userAwardMap != null
                && userAwardMap.getUser().getUserId() == user.getUserId()) {
            //获取时间戳用于有效性验证
            long timeStamp = System.currentTimeMillis();
            //设置二维码内容
            //冗余aaa为了后期替换
            String content = "{aaauserAwardIdaaa:"+ userAwardId + ",aaacustomerIdaaa:"
                    + user.getUserId() + ",aaacreateTimeaaa:" + timeStamp + "}";
            // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
            try {
                String longUrl = urlPrefix + exchangeUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
                //获取短链接
                String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
                BitMatrix bitMatrix = CodeUtil.generateQRCodeStream(shortUrl, response);
                //将二维码发送到前端
                MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
            } catch (Exception e) {
                logger.error("二维码创建失败");
            }
        }
    }


}
