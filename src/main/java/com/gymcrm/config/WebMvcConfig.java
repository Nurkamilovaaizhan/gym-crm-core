package com.gymcrm.config;

import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.configuration.SpringDocSpecPropertiesConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.gymcrm.controller")
@Import({
        SpringDocConfiguration.class,
        SpringDocSpecPropertiesConfiguration.class,
        SpringDocWebMvcConfiguration.class,
        MultipleOpenApiSupportConfiguration.class,
        SwaggerConfig.class,
        SpringDocConfigProperties.class,
        SwaggerUiConfigProperties.class,
        SwaggerUiOAuthProperties.class,
        JacksonAutoConfiguration.class
})
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("gym-crm")
                .pathsToMatch("/api/**")
                .build();
    }
}