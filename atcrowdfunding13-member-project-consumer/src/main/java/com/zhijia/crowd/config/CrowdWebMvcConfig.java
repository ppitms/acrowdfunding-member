package com.zhijia.crowd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhijia
 * @create 2022-03-30 23:03
 */
@Configuration
public class CrowdWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 点击发起众筹
        registry.addViewController("/agree/protocol/page").setViewName("project-agree");
        // 点击同意协议
        registry.addViewController("/launch/project/page").setViewName("project-launch");
        //填完表单到下一个页面(回报页面)
        registry.addViewController("/return/info/page").setViewName("project-return");
        //填完表单到下一个页面
        registry.addViewController("/create/confirm/page").setViewName("project-confirm");
        //成功提交页面
        registry.addViewController("/create/success").setViewName("project-success");
    }
}
