package com.corebanking.apigateway;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayConfig {

  @Bean
  public RouterFunction<ServerResponse> clientServiceRoute() {
    return GatewayRouterFunctions.route(
        RequestPredicates.path("/api/v1/clients/**"), HandlerFunctions.http());
  }

  @Bean
  public RouterFunction<ServerResponse> savingsProductsRoute() {
    return GatewayRouterFunctions.route(
        RequestPredicates.path("/api/v1/savings-products/**"), HandlerFunctions.http());
  }

  @Bean
  public RouterFunction<ServerResponse> savingsAccountsRoute() {
    return GatewayRouterFunctions.route(
        RequestPredicates.path("/api/v1/savings-accounts/**"), HandlerFunctions.http());
  }

  @Bean
  public RouterFunction<ServerResponse> fixedDepositsRoute() {
    return GatewayRouterFunctions.route(
        RequestPredicates.path("/api/v1/fixed-deposits/**"), HandlerFunctions.http());
  }

  @Bean
  public RouterFunction<ServerResponse> ledgerRoute() {
    return GatewayRouterFunctions.route(
        RequestPredicates.path("/api/v1/ledger/**"), HandlerFunctions.http());
  }
}
