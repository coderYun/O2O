package com.hly.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {

    /**
     * 首页路由
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    private String index() {
        return "frontend/index";
    }

    /**
     * 商品列表路由
     *
     * @return
     */
    @RequestMapping(value = "/shoplist", method = RequestMethod.GET)
    private String showShopList() {
        return "frontend/shoplist";
    }

    /**
     * 店铺详情路由
     *
     * @return
     */
    @RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
    private String showShopDetail() {
        return "frontend/shopdetail";

    }

    /**
     * 商品详情路由
     */
    @RequestMapping(value = "/productdetail", method = RequestMethod.GET)
    private String showProductDetail() {
        return "frontend/productdetail";
    }

    //转发至奖品兑换页面
    @RequestMapping(value = "/awardlist", method = RequestMethod.GET)
    private String awardList() {
        return "frontend/awardlist";
    }

    //奖品兑换列表路由
    @RequestMapping(value = "/pointrecord",method = RequestMethod.GET)
    private String showPointRecord(){
        return "frontend/pointrecord";
    }

    //奖品详情页的路由
    @RequestMapping(value = "/myawarddetail",method = RequestMethod.GET)
    private String shopMyAwardDetail(){
        return "frontend/myawarddetail";
    }

    //消费记录路由
    @RequestMapping(value = "/myrecord",method = RequestMethod.GET)
    private String myRecord(){
        return "frontend/myrecord";
    }
    //用户各店铺积分路由
    @RequestMapping(value = "mypoint",method = RequestMethod.GET)
    private String MyPoint(){
        return "frontend/mypoint";
    }
}
