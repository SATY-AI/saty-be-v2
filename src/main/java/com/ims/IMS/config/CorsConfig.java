package com.ims.IMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig{

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // Enable CORS globally for all endpoints
//        http.cors();
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://3.27.215.5:3000", "http://3.27.215.5:3001",
                                "http://localhost:3000", "http://localhost:3001", "http://3.27.215.5:8999",
                                "http://3.27.215.5:8080","http://ims.saty.vn", "http://ims.saty.vn:8999",
                                "http://ims.saty.vn:3000", "http://ims.saty.vn:3001","http://3.27.215.5:3002",
                                "http://3.27.215.5:3003","http://3.27.215.5:3100","http://3.27.215.5:3200",
                                "http://3.27.215.5:3300","http://3.27.215.5:3400","http://3.27.215.5:3500",
                                "http://3.27.215.5:3004", "http://journey.saty.vn", "http://awd.saty.vn",
                                "http://agri.saty.vn", "http://mrv1.saty.vn", "https://ims.saty.vn",
                                "https://agri.saty.vn", "https://mrv1.saty.vn", "https://journey.saty.vn",
                                "https://awd.saty.vn")
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Add the HTTP methods you want to allow
                        .allowedHeaders("*");
            }
        };
    }
}
