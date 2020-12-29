package cn.gmj.taotao.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        // 添加Cors配置信息
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://manage.taotao.com");
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("HEAD");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedHeader("*");
        // 配置拦截路径
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(configurationSource);


    }
}
