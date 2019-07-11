package com.hly.o2o.web.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hly.o2o.dto.AwardExecution;
import com.hly.o2o.dto.ImageHolder;
import com.hly.o2o.dto.UserAwardMapExecution;
import com.hly.o2o.entity.Award;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.entity.UserShopMap;
import com.hly.o2o.enums.AwardStateEnum;
import com.hly.o2o.service.AwardService;
import com.hly.o2o.service.UserAwardMapService;
import com.hly.o2o.service.UserShopMapService;
import com.hly.o2o.util.CodeUtil;
import com.hly.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopAwardController {

    @Autowired
    private UserShopMapService userShopMapService;
    @Autowired
    private AwardService awardService;


    @RequestMapping(value = "/listawardsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listAwardsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        // 店铺Id
        long shopId = HttpServletRequestUtil.getlong(request, "shopId");
        // 空值判断
        if (pageIndex >= 0 && pageSize >= 0 && shopId >= 0) {
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            Award awardCondition = compactProductCondition(shopId, awardName);
            // 分页获取该店铺下的顾客积分列表
            AwardExecution ad = awardService.getAwardList(awardCondition, pageIndex, pageSize);
            modelMap.put("awardList", ad.getAwardList());
            modelMap.put("count", ad.getCount());
            modelMap.put("success", true);
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user != null && user.getUserId() != null) {
                UserShopMap userShopMap = userShopMapService.getUserShopMap(user.getUserId(), shopId);
                if (userShopMap == null)
                    modelMap.put("totalPoint", 0);
                else
                    modelMap.put("totalPoint", userShopMap.getPoint());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty info");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getawardbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getAwardById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取前端传过来的awardId
        long awardId = HttpServletRequestUtil.getlong(request, "awardId");
        //空值判断i
        if (awardId > -1) {
            Award award = awardService.getAwardById(awardId);
            modelMap.put("award", award);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "awardId is Empty");
        }
        return modelMap;
    }


    @ResponseBody
    @RequestMapping(value = "addaward", method = RequestMethod.GET)
    private Map<String, Object> addAward(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //验证码校验
        //根据传入的值决定是否需要跳过验证码输入
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        //接收前端参数如变量的初始化，包过奖品，缩略图
        ObjectMapper mapper = new ObjectMapper();
        Award award = null;
        String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //缩略图空值判断
        try {
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail);
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        try {
            //将前端传过来的string流转化为product实体类
            award = mapper.readValue(awardStr, Award.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        //空值判断
        if (award != null && thumbnail != null) {
            try {

                //从session中获取店铺信息
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());
                //添加award
                AwardExecution ae = awardService.addAward(award, thumbnail);
                if (ae.getState() == AwardStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ae.getStateInfo());
                }

            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");

        }
        return modelMap;


    }


    /**
     * 封装商品查询条件到Product实例中
     *
     * @param shopId
     * @param awardName
     */
    private Award compactProductCondition(long shopId, String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);

        // 若有商品名模糊查询的要求则添加进去
        if (awardName != null) {
            awardCondition.setAwardName(awardName);
        }
        return awardCondition;
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

    @RequestMapping(value = "/modifyaward", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> modifyAward(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        //根据传入的值决定是否需要跳过验证码输入
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        //接收前端参数，包过商品，缩略图
        ObjectMapper mapper = null;
        Award award = null;
        ImageHolder thumbnail = null;

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //缩略图空值判断
        try {
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail);
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        try {
            String awardStr = HttpServletRequestUtil.getString(request, "awardstr");
            //将前端传过来的string流转化为product实体类
            award = mapper.readValue(awardStr, Award.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        //空值判断
        if (award != null) {
            try {
                //从session中获取当前店铺
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());
                AwardExecution ae = awardService.modifyAward(award, thumbnail);
                if (ae.getState() == AwardStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ae.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

}
