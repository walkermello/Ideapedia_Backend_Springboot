//package com.tugasakhir.ideapedia.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*") // Allow all origins (you can restrict this for production)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD") // Allowed methods
//                .allowedHeaders("*") // Allow all headers
//                .allowCredentials(true); // Allow credentials if necessary
//    }
//}
