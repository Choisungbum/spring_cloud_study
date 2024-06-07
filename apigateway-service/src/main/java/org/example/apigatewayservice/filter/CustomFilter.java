package org.example.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
// AbstractGatewayFilterFactory<> 반드시 상속받아야 한다.
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Costom Pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE filter : request id -> {}", request.getId());

            // CUstop Post Filter
            // Mono라는 객체는 웹플럭스에서 추가되어 있는 기능
            // -> 기존의 동기방식의 서버가 아니라 비동기방식의 서버를 지원할 때 단일 값 전달할 때는 모노타입으로 전달
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Custom POST filter : response id -> {}", response.getStatusCode());
            }));
        };
    }


    public static class Config {
        // Put the configuration properties
    }
}
