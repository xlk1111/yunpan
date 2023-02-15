package com.our.yun.config;

import com.our.yun.controller.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class SpringMvcSupport extends WebMvcConfigurationSupport {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/img/**").addResourceLocations("/img/");
        //registry.addResourceHandler("/META-INF/**").addResourceLocations("/META-INF/");
        registry.addResourceHandler("/WEB-INF/**").addResourceLocations("/WEB-INF/");
    }

//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor);
//    }
}
