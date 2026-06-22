
package com.corebanking.savingsservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI savingsServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Savings Service API")
                        .description("Core Banking - Savings Management Service")
                        .version("1.0"));
    }
}