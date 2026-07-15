package com.corebanking.fineractintegration.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI fineractIntegrationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fineract Integration Service API")
                        .description("Core Banking - Fineract Integration Layer")
                        .version("1.0"));
    }
}