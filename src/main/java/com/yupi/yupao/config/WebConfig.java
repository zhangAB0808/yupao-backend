package com.yupi.yupao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 拦截器跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /*
            // 跨域路径
            CorsRegistration cors = registry.addMapping("/**");
            // 可访问的外部域
            cors.allowedOrigins("http://localhost:8002");
            // 支持跨域用户凭证
            cors.allowCredentials(true);
            cors.allowedOriginPatterns("*");
            // 设置header能携带的信息
            cors.allowedHeaders("*");
            // 设置支持跨域的请求方法
            cors.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION");
            // 设置跨域过期时间
            cors.maxAge(3600);
        */
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION")
                .maxAge(3600);
    }

}
