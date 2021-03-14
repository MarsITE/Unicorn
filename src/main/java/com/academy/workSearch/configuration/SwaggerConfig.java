package com.academy.workSearch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig{

    private static final String TITLE = "workSearch REST API Documentation";
    private static final String DESCRIPTION = "REST API Documentation";
    private static final String VERSION = "1.0";
    private static final String FREE = "Free to use";
    private static final Contact CONTACT = new Contact(
            "Andrii Prybyla", "https://gitlab.com/MarsITE", "mars1985@ukr.net");
    private static final String LICENSE = "SoftServe Academy";
    private static final String SITE = "https://softserve.academy/";

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.academy.workSearch"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(TITLE, DESCRIPTION, VERSION, FREE, CONTACT, LICENSE, SITE, Collections.emptyList());
    }

}
