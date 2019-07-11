package com.hly.o2o.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.hly.o2o.entity.PersonInfo;
import com.hly.o2o.entity.Product;
import com.hly.o2o.service.ProductService;
import com.hly.o2o.util.CodeUtil;
import com.hly.o2o.util.HttpServletRequestUtil;
import com.hly.o2o.util.ShortNetAddressUtil;
import com.hly.o2o.web.shopadmin.ShopAuthManagementController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class ProductDetailController {

    private Logger logger = LoggerFactory.getLogger(ProductDetailController.class);
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/listproductdetailpageinfo")
    @ResponseBody
    private Map<String, Object> ListProductDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        long productId = HttpServletRequestUtil.getlong(request, "productId");
        Product product = null;
        //非空判断
        if (productId != -1) {
            try {
                product = productService.getProductById(productId);
                PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
                if (user == null) {
                    modelMap.put("needQRCode", false);
                } else {
                    modelMap.put("needQRCode", true);

                }
                modelMap.put("product", product);
                modelMap.put("success", true);

            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "productId is empty");
        }
        return modelMap;

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
        ProductDetailController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ProductDetailController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ProductDetailController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        ProductDetailController.authUrl = authUrl;
    }

    /**
     * 生成二维码图片流并返回给前端
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
        long productId = HttpServletRequestUtil.getlong(request, "productId");
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (productId != -1 && user != null && user.getUserId() != null) {
            //获取时间戳用于有效性验证
            long timeStamp = System.currentTimeMillis();
            //设置二维码内容
            //冗余aaa为了后期替换
            String content = "{aaaproductIdaaa:" + productId + ",aaacustomerIdaaa:"
                    + user.getUserId() + ",aaacreateTimeaaa:" + timeStamp + "}";
            // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
            try {
                String longUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
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
