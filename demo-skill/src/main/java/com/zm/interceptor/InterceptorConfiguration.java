package com.zm.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Administrator on 2017/9/27.
 */
@Configuration
public class InterceptorConfiguration {

    @Bean
    public Interceptor interceptor() {
        return new Interceptor();
    }

    static class Interceptor extends WebMvcConfigurerAdapter {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            // 注册拦截器
            InterceptorRegistration ir = registry.addInterceptor(new ControllerInterceptor());
            // 配置拦截的路径
            //ir.addPathPatterns("");
            // 配置不拦截的路径
            ir.excludePathPatterns("/**.html");
            ir.excludePathPatterns("/swagger-resources/**");
            ir.excludePathPatterns("/");


            // 还可以在这里注册其它的拦截器
            //registry.addInterceptor(new OtherInterceptor()).addPathPatterns("/**");
        }
    }


}
