package com.hly.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hly.o2o.dto.ImageHolder;
import com.hly.o2o.dto.ProductExecution;
import com.hly.o2o.entity.Product;
import com.hly.o2o.entity.ProductCategory;
import com.hly.o2o.entity.ProductImg;
import com.hly.o2o.entity.Shop;
import com.hly.o2o.enums.ProductStateEnum;
import com.hly.o2o.exceptions.ProductOperationException;
import com.hly.o2o.service.ProductCategoryService;
import com.hly.o2o.service.ProductService;
import com.hly.o2o.util.CodeUtil;
import com.hly.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;

	// 支持上传商品详情图的最大数量
	private static final int IMAGEMAXCOUNT = 6;
    @RequestMapping(value="/getproductlistbyshop",method=RequestMethod.GET)
    @ResponseBody
	private Map<String, Object> getProductListByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取前台传来的页码
		int pageIndex = HttpServletRequestUtil.getInt(request,"pageIndex");
		//获取每页要求返回多少商品数量
		int pageSize=HttpServletRequestUtil.getInt(request, "pageSize");
		//从当前session中获取要操作的店铺
		Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
		//非空判断
		if((pageIndex>-1) && (pageSize>-1) && (currentShop!=null) && (currentShop.getShopId()!=null)){
			//组合要筛选的条件
			long productCategoryId=HttpServletRequestUtil.getInt(request, "productCategoryId");
			String productName=HttpServletRequestUtil.getString(request, "productName");
			Product productCondition=compactCondition(currentShop.getShopId(),productCategoryId,productName);
			//传入查询条件并返回相应的商品列表以及分页信息商品列表及总数
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
			
			
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "pageSize or pageIndex or shopid is empty!");
		}
		return modelMap;
			

	}
	
	

	private Product compactCondition(Long shopId, long productCategoryId, String productName) {
		Product productCondition=new Product();
		Shop shop=new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		//若有指定类别的要求则也添加到查询条件中去
		if(productCategoryId!=-1L){
			ProductCategory pc=new ProductCategory();
			pc.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(pc);
		}
		
		//若有商品名称也添加到查询条件中去
		if(productName!=null){
			productCondition.setProductName(productName);
		}
		
		
		return productCondition;
	}



	/**
	 * 通过ProductID唯一查询商品信息
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductById(@RequestParam Long productId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 空值判断
		if (productId > -1) {
			// 获取商品信息
			Product product = productService.getProductById(productId);
			// 获取该店铺下的所有商品类别列表
			List<ProductCategory> productCategoryList = productCategoryService
					.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productid");
		}

		return modelMap;
	}

	@RequestMapping(value = "/addproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 验证码校验
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}

		// 接收前端传过来的参数变量
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		// 用来处理文件流
		MultipartHttpServletRequest mutilpartRequest = null;
		// 用来处理缩略图
		ImageHolder thumbnail = null;
		// 用来保存商品详情图的商品列表
		List<ImageHolder> productImageList = new ArrayList<ImageHolder>();
		// 从request.session中获取到文件流
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			// 若请求中存在文件流，则取出相关的文件（包过缩略图和详情图）
			if (multipartResolver.isMultipart(request)) {
				mutilpartRequest = (MultipartHttpServletRequest) request;
				// 取出缩略图并且构建ImageHolder对象
				CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) mutilpartRequest.getFile("thumbnail");
				thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());

				// 取出详情图列表并构建List<ImageHolder>对象,最多支持上传六张
				for (int i = 0; i < IMAGEMAXCOUNT; i++) {
					CommonsMultipartFile productImgFile = (CommonsMultipartFile) mutilpartRequest
							.getFile("productImg" + i);
					if (productImgFile != null) {
						// 若取出的第i个详情图片的文件流不为空，则将其加入详情图列表
						ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
								productImgFile.getInputStream());
						productImageList.add(productImg);
					} else {
						// 若取出的第i个详情图片文件流为空了，则跳出循环
						break;
					}
				}
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传的图片不能为空");
				return modelMap;

			}

		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;

		}

		try {
			// 将前端传过来的表单String流并将其转换成Product实体类
			product = mapper.readValue(productStr, Product.class);

		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;

		}
		// 若Product信息，缩略图，商品详情图列表都不为空，那么就开始进行商品的添加
		if (product != null && thumbnail != null && productImageList.size() > 0) {
			try {
				// 从session中获取当前店铺的ID并给其添加商品,这样为了减少对前端的依赖
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				Shop shop = new Shop();
				shop.setShopId(currentShop.getShopId());
				product.setShop(shop);
				// 执行添加操作
				ProductExecution pe = productService.addProduct(product, thumbnail, productImageList);
				if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}

			} catch (ProductOperationException e) {
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

	@RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 先判断是商品编辑调用还是商品上下架的调用,前者要进行验证码的校验，而后者不需要验证码的校验
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		// 验证码的判断
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 接收前端参数变量的初始化，包过商品，缩略图，详情图，以及实体类表
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 若请求中存在文件流，则取出相应的缩略图和详情图
		try {
			if (multipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest mHttpServletRequest = (MultipartHttpServletRequest) request;
				// 取出缩略图并构建ImageHolder对象
				CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) mHttpServletRequest.getFile("thumbnail");
				if (thumbnailFile != null) {
					thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
				}
				// 取出详情图列表并构建List<ProductImg>对象，最多支持上传6张
				for (int i = 0; i < IMAGEMAXCOUNT; i++) {
					CommonsMultipartFile productImgFile = (CommonsMultipartFile) mHttpServletRequest
							.getFile("productImg" + i);
					if (productImgFile != null) {
						// 若取出的详情图不为空，则将其加入到详情图列表中去
						ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
								productImgFile.getInputStream());
						productImgList.add(productImg);
					} else {
						// 若取出的文件流为空，则终止循环
						break;
					}

				}

			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;

		}
		try {
			String productStr = HttpServletRequestUtil.getString(request, "productStr");
			// 将前端传过来的String流转化为Product对象
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		// 非空判断
		if (product != null) {
			try {
				// 从session中获取当前店铺的ID并赋值给Product,减少对前端的依赖
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				Shop shop = new Shop();
				shop.setShopId(currentShop.getShopId());
				product.setShop(shop);
				// 更新店铺的信息
				ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
				if (ProductStateEnum.SUCCESS.getState() == pe.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (Exception e) {
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
