package com.hly.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopadmin", method = { RequestMethod.GET })
public class ShopAdminController {
	@RequestMapping(value = "/shopoperation", method = { RequestMethod.GET })
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
	@RequestMapping(value = "/productcategorymanagement",method=RequestMethod.GET)
	public String ProductCategoryManagement() {
		return "shop/productcategorymanagement";
		
	}
	@RequestMapping(value="/productoperation")
	public String productOperation(){
		//转发到商品添加编辑页面
		return "shop/productoperation";
		
	}
	@RequestMapping(value = "/productmanagement",method=RequestMethod.GET)
	public String ProductManagement(){
		//转发至商品管理页面
		return "shop/productmanagement";
	}

}
