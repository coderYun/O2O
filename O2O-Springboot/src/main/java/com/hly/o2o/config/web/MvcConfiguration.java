package com.hly.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.hly.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import com.hly.o2o.interceptor.shopadmin.ShopPermissionInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.ServletException;

/**
 * WebMvcConfigurerAdapter:配置视图解析器
 *
 * 当一个类实现了ApplicationContextAware之后，这个类就可以方便的获取ApplicationContext中的所有的bean了
 */

//等价于spring-web.xml的<mvc:annotation-driver/>
@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {

    //Spring容器
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;

    }
    /**
     * 静态资源的配置
     *
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");

        //window下的图片路径配置
       registry.addResourceHandler("/upload/**").addResourceLocations("file:F:/projectdev/image/upload/");

        //Linux下的图片路径
       //registry.addResourceHandler("/upload/**").addResourceLocations("file:/home/hly/image/upload/");

    }

    /**
     * 定义默认的请求处理器
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();

    }

    /**
     * 创建viewResolver视图解析器
     */
    @Bean(name="viewResolver")
    public ViewResolver createViewResolver(){
        InternalResourceViewResolver viewResolver=new InternalResourceViewResolver();
        //设置spring容器
        viewResolver.setApplicationContext(this.applicationContext);
        //取消缓存
        viewResolver.setCache(false);
        //设置解析前缀
        viewResolver.setPrefix("/WEB-INF/html/");
        //设置解析后缀
        viewResolver.setSuffix(".html");
        return viewResolver;

    }

    /**
     * 文件上传解析器
     */
    @Bean(name="multipartResolver")
    public CommonsMultipartResolver createCommonsMultipartResolver(){
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;

    }

    //验证码的配置
    @Value("${kaptcha.border}")
    private String border;

    @Value("${kaptcha.textproducer.font.color}")
    private String fcolor;

    @Value("${kaptcha.image.width}")
    private String width;

    @Value("${kaptcha.textproducer.char.string}")
    private String cString;

    @Value("${kaptcha.image.height}")
    private String height;

    @Value("${kaptcha.textproducer.font.size}")
    private String fsize;

    @Value("${kaptcha.noise.color}")
    private String nColor;

    @Value("${kaptcha.textproducer.char.length}")
    private String clength;

    @Value("${kaptcha.textproducer.font.names}")
    private String fnames;

    /**
     * 由于web.xml在springboot中不生效，所以要手动配置Kaptcha验证码Servlet
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
        servlet.addInitParameter("kaptcha.border",border);
        servlet.addInitParameter("kaptcha.textproducer.font.color",fcolor);
        servlet.addInitParameter("kaptcha.image.width",width);
        servlet.addInitParameter("kaptcha.textproducer.char.string",cString);
        servlet.addInitParameter("kaptcha.image.height",height);
        servlet.addInitParameter("kaptcha.textproducer.font.size",fsize);
        servlet.addInitParameter("kaptcha.noise.color",nColor);
        servlet.addInitParameter("kaptcha.textproducer.char.length",clength);
        servlet.addInitParameter("kaptcha.textproducer.font.names",fnames);
        return servlet;

    }

    /**
     * 添加拦截器的配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        String interceptorPath="/shopadmin/**";
        //注册一个拦截器(店家登录系统的拦截器)
        InterceptorRegistration loginInterceptor = registry.addInterceptor(new ShopLoginInterceptor());
        //配置拦截的路径
        loginInterceptor.addPathPatterns(interceptorPath);
        loginInterceptor.excludePathPatterns("/shopadmin/addshopauthmap");

        //注册一个拦截器(对店家系统进行操作的拦截器)
        InterceptorRegistration pessionInterceptor = registry.addInterceptor(new ShopPermissionInterceptor());
        //进行拦截器路径的配置
        pessionInterceptor.addPathPatterns(interceptorPath);

        //配置不需要拦截的路径
        //shoplist page
        pessionInterceptor.excludePathPatterns("/shopadmin/shoplist");
        pessionInterceptor.excludePathPatterns("/shopadmin/getshoplist");

        //shopregister pages
        pessionInterceptor.excludePathPatterns("/shopadmin/getshopinitinfo");
        pessionInterceptor.excludePathPatterns("/shopadmin/registershop");
        pessionInterceptor.excludePathPatterns("/shopadmin/shopoperation");

        //shopmanage pages
        pessionInterceptor.excludePathPatterns("/shopadmin/shopmanagement");
        pessionInterceptor.excludePathPatterns("/shopadmin/getshopmanagementinfo");
        pessionInterceptor.excludePathPatterns("/shopadmin/addshopauthmap");



    }





}
