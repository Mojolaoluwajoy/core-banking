package com.corebanking.fixeddepositservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI fixedDepositServiceOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Fixed Deposit Service API")
                .description("Core Banking - Fixed Deposit Management Service")
                .version("1.0"));
  }
}
