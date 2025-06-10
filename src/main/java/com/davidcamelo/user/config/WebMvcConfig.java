package com.davidcamelo.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:4173", "http://localhost:4174", "http://localhost:4175", "http://localhost:4176",
                        "http://localhost:5173", "http://localhost:5174", "http://localhost:5175", "http://localhost:5176",
                        "https://components-ui.davidcamelo.com", "https://user-ui.davidcamelo.com",
                        "https://product-ui.davidcamelo.com", "https://react-ui.davidcamelo.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "TRACE", "CONNECT")
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true);
    }
}
