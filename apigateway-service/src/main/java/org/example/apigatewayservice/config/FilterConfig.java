package org.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/first-service/**") // 사용자가 해당 서비스를 호출하면
                        .filters(f -> f.addRequestHeader("first-request", "first-request-header") // request 필터에는 해당 데이터 추가
                                       .addResponseHeader("first-response", "first-response-header")) // response 필터에는 해당 데이터 추가
                        .uri("http://localhost:8081")) // 해당 주소로 이동한다.
                .route(r -> r.path("/second-service/**")
                        .filters(f -> f.addRequestHeader("second-request", "second-request-header")
                                       .addResponseHeader("second-response", "second-response-header"))
                        .uri("http://localhost:8082"))
                .build();
    }
}
