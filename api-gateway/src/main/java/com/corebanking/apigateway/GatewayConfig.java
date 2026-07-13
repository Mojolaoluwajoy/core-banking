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
        return GatewayRouterFunctions.route()
                .route(RequestPredicates.path("/api/v1/clients/**"),
                        HandlerFunctions.http("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> savingsProductsRoute() {
        return GatewayRouterFunctions.route()
                .route(RequestPredicates.path("/api/v1/savings-products/**"),
                        HandlerFunctions.http("http://localhost:8085"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> savingsAccountsRoute() {
        return GatewayRouterFunctions.route()
                .route(RequestPredicates.path("/api/v1/savings-accounts/**"),
                        HandlerFunctions.http("http://localhost:8082"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fixedDepositsRoute() {
        return GatewayRouterFunctions.route()
                .route(RequestPredicates.path("/api/v1/fixed-deposits/**"),
                        HandlerFunctions.http("http://localhost:8083"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> ledgerRoute() {
        return GatewayRouterFunctions.route()
                .route(RequestPredicates.path("/api/v1/ledger/**"),
                        HandlerFunctions.http("http://localhost:8084"))
                .build();
    }
}