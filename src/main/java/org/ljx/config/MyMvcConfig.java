package org.ljx.config;

import org.apache.catalina.core.ApplicationContext;
import org.ljx.interceptor.MyInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ljx on 2018/5/15.
 */
@Configuration
@EnableWebMvc
@ComponentScan
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    public MyMvcConfig(){
        super();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/templates/");

        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截规则：除了login，其他都拦截判断
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/error");
        super.addInterceptors(registry);
    }

}