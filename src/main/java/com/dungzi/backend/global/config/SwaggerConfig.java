package com.dungzi.backend.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public static final String TITLE="Dunji-Rest API";
    public static final String DESCRIPTION="Dunji API명세서";
    public static final String VERSION="v0.0.1";
    private static final String BASIC_API_PATH="/api/v1";

    @Bean
    public GroupedOpenApi user() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch(BASIC_API_PATH+"/users/**")
                .build();
    }
    @Bean
    public GroupedOpenApi auth() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch(BASIC_API_PATH+"/auth/**")
                .build();
    }
    @Bean
    public GroupedOpenApi chat() {
        return GroupedOpenApi.builder()
                .group("chat")
                .pathsToMatch(BASIC_API_PATH+"/chat/**")
                .build();
    }
    @Bean
    public GroupedOpenApi review() {
        return GroupedOpenApi.builder()
                .group("review")
                .pathsToMatch(BASIC_API_PATH+"/review/**")
                .build();
    }
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION));
    }
}
