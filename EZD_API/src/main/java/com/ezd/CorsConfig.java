package com.ezd;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Xác định chính xác URL của ứng dụng React của bạn (loại bỏ dấu gạch cuối cùng)
        config.addAllowedOrigin("http://localhost:3000/");
        config.addAllowedOrigin("http://localhost:3001/");
      
        	config.addAllowedOrigin("  http://localhost:51899/");
        config.addAllowedHeader("*");
        
        // Sử dụng setAllowedMethods để xác định danh sách các phương thức được phép
        config.setAllowedMethods(Arrays.asList("OPTIONS", "GET", "POST", "PUT", "DELETE"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
