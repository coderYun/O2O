package com.hly.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopadmin", method = {RequestMethod.GET})
public class ShopAdminController {
    @RequestMapping(value = "/shopoperation", method = {RequestMethod.GET})
    public String shopOperation() {
        return "shop/shopoperation";

    }

    @RequestMapping(value = "/shoplist")
    public String shopList() {
        return "shop/shoplist";

    }

    @RequestMapping(value = "/shopedit", method = RequestMethod.GET)
    private String shopEdit() {
        return "shop/shopedit";
    }

    @RequestMapping(value = "/shopmanagement")
    public String shopManagement() {
        return "shop/shopmanagement";

    }

    @RequestMapping(value = "/productcategorymanagement", method = RequestMethod.GET)
    public String ProductCategoryManagement() {
        return "shop/productcategorymanagement";

    }

    @RequestMapping(value = "/productoperation")
    public String productOperation() {
        //转发到商品添加编辑页面
        return "shop/productoperation";

    }

    @RequestMapping(value = "/productmanagement", method = RequestMethod.GET)
    public String ProductManagement() {
        //转发至商品管理页面
        return "shop/productmanagement";
    }

    @RequestMapping(value = "/shopauthmanagement")
    private String ShopAuthManagement() {
        //转发至授权管理页面
        return "shop/shopauthmanagement";
    }

    @RequestMapping(value = "/shopauthedit")
    private String ShopAuthEdit() {
        //转发至授权信息修改页面
        return "shop/shopauthedit";

    }

    //扫码操作失败时的页面
    @RequestMapping(value = "/operationfail", method = RequestMethod.GET)
    private String operationFail() {
        return "shop/operationfail";
    }

    //扫码操作成功时的页面
    @RequestMapping(value = "/operationsuccess", method = RequestMethod.GET)
    private String operationSuccess() {
        return "shop/operationsuccess";
    }

    //转发至店铺消费的路由页面‘
    @RequestMapping(value = "/productbuycheck", method = RequestMethod.GET)
    private String productBuyCheck() {
        return "shop/productbuycheck";
    }

    //转发至用户积分页面
    @RequestMapping(value = "/usershopcheck", method = RequestMethod.GET)
    private String userShopCheck() {
        return "shop/usershopcheck";
    }

    //转发至店铺用户积分页面
    @RequestMapping(value = "/awarddelivercheck", method = RequestMethod.GET)
    private String awardDeliver() {
        return "shop/awarddelivercheck";
    }

    //转发至奖品管理页面
    @RequestMapping(value = "/awardmanagement", method = RequestMethod.GET)
    private String awardMangement() {
        return "shop/awardmanagement";
    }

    //转发至奖品管理页面
    @RequestMapping(value = "/awardoperation", method = RequestMethod.GET)
    private String awardOperation() {
        return "shop/awardoperation";
    }

}
