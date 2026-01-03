package com.onebrain.coupons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coupons API")
                        .version("1.0.0")
                        .description("API para gerenciamento de cupons de desconto")
                        .contact(new Contact()
                                .name("Onebrain Challenge")
                                .email("contact@onebrain.com")));
    }
}