package com.springboot.examplework.core.constaint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition
@Configuration
public class SpringDocConfig {
  @Bean
  public OpenAPI baseOpenAPI() {
    final String securitySchemeName = "bearerAuth";

    return new OpenAPI()
        .info(new Info()
              .title("API Documentation - ExampleWork")
              .description("SpringBoot 3.2.5 application")
              .version("v1.0.0")
              .license(new License().name("授權訊息").url("http://springdoc.org"))
              .contact(new Contact().name("wayne.w").email("wayne.w@webglsoft.com"))
        )
        .components(new Components()
              .addSecuritySchemes( securitySchemeName, new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
              )
        );
    }
}