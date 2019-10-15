package com.softplayer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${info.application.name}")
    private String name;
    
    @Value("${info.application.description}")
    private String description;
    
    @Value("${info.application.version}")
    private String version;

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(name)
                .description(description)
                .license("")
                .licenseUrl("")
                .termsOfServiceUrl("")
                .version(version)
                .build();
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.softplayer"))
                .build()
                .apiInfo(apiInfo());
    }
}