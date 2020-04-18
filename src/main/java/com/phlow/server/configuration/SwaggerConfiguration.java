package com.phlow.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    @Value("${phlow.api.prefix:#/api}")
    private String apiPrefix;

    private final Environment environment;

    @Autowired
    public SwaggerConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .apiInfo(this.apiInfo())
                .select()
                .paths(regex(apiPrefix + "/.*"))
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfo(
                this.environment.getProperty("phlow.documentation.title"),
                this.environment.getProperty("phlow.documentation.description"),
                this.environment.getProperty("phlow.documentation.version"),
                this.environment.getProperty("phlow.documentation.termsOfServiceUrl"),
                new Contact(
                        this.environment.getProperty("phlow.documentation.contact.name"),
                        this.environment.getProperty("phlow.documentation.contact.url"),
                        this.environment.getProperty("phlow.documentation.contact.email")),
                this.environment.getProperty("phlow.documentation.license"),
                this.environment.getProperty("phlow.documentation.licenseUrl"),
                new ArrayList<VendorExtension>()
        );
    }
}
