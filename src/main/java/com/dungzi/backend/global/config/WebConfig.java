package com.dungzi.backend.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    public static final String ALLOWED_METHOD_NAMES = "GET,POST,PATCH,PUT,DELETE,OPTIONS";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 요청 도메인
                .allowedMethods(ALLOWED_METHOD_NAMES.split(",")) // 요청 메서드
                .allowCredentials(true) // 쿠키 전송 허용
//                .maxAge(3000) // 원하는 시간만큼 pre-flight 리퀘스트 캐싱
                ;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
