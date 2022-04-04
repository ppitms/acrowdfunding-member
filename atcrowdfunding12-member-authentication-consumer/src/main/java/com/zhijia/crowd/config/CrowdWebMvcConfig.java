package com.zhijia.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhijia
 * @create 2022-03-28 20:34
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

//        registry.addViewController("/").setViewName("portal");
        //注册页面
        registry.addViewController("/auth/member/to/reg/page").setViewName("member-reg");
        //登录页面
        registry.addViewController("/auth/member/to/login/page").setViewName("member-login");
        //主页面
        registry.addViewController("/auth/member/to/center/page").setViewName("member-center");
        //点击我的众筹
        registry.addViewController("/member/my/crowd").setViewName("member-crowd");
    }
}
