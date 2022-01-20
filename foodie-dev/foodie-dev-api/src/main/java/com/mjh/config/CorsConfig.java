package com.mjh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @PackageName: com.mjh.config
 * @ClassName: CorsConfig
 * @Author: majiahuan
 * @Date: 2020/10/24 22:20
 * @Description:
 */

@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加cors配置信息
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");
        // 2. 设置是否发送cookie信息
        configuration.setAllowCredentials(true);
        // 3. 设置允许请求的方式
        configuration.addAllowedMethod("*");
        // 4. 设置允许的header
        configuration.addAllowedHeader("*");
        // 5. 为url添加映射路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        // 6. 返回新的source
        return new CorsFilter(source);
    }
}
