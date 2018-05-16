package org.ljx.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ljx on 2018/5/15.
 */
//@Configuration
//@EnableWebMvc
//@ComponentScan("com.example.spring.framework.converter")
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 配置自定义的HttpMessageConverter 的Bean ，在Spring MVC 里注册HttpMessageConverter有两个方法：
     * 1、configureMessageConverters ：重载会覆盖掉Spring MVC 默认注册的多个HttpMessageConverter
     * 2、extendMessageConverters ：仅添加一个自定义的HttpMessageConverter ，不覆盖默认注册的HttpMessageConverter
     * 在这里重写extendMessageConverters
     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(converter());
//    }
//
//    @Bean
//    public MyConverter converter() {
//        return new MyConverter();
//    }

}