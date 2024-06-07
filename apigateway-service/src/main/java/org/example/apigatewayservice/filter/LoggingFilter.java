package org.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
// AbstractGatewayFilterFactory<> 반드시 상속받아야 한다.
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Costom Pre Filter
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Global Filter BaseMessage : {}", config.getBaseMessage());
//
//            if (config.isPreLogger()) {
//                log.info("Global Filter Start : request id -> {}", request.getId());
//            }
//
//            // CUstop Post Filter
//            // Mono라는 객체는 웹플럭스에서 추가되어 있는 기능
//            // -> 기존의 동기방식의 서버가 아니라 비동기방식의 서버를 지원할 때 단일 값 전달할 때는 모노타입으로 전달
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                if (config.isPostLogger()) {
//                    log.info("Global Filter End : request id -> {}", response.getStatusCode());
//                }
//            }));
//        };

        // Global 필터는 인스턴스 이기 때문에 직접 생성할 수 없다.
        // OrderedGatewayFilter는 두 번째 매개변수가 필요한데 해당 자리에 필터의 우선순위를 정할 수 있는 것을 넣어주면 된다.
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter BaseMessage : {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Logging Filter Start : request id -> {}", request.getId());
            }

            // CUstop Post Filter
            // Mono라는 객체는 웹플럭스에서 추가되어 있는 기능
            // -> 기존의 동기방식의 서버가 아니라 비동기방식의 서버를 지원할 때 단일 값 전달할 때는 모노타입으로 전달
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("Logging Filter End : request id -> {}", response.getStatusCode());
                }
            }));
        }, OrderedGatewayFilter.HIGHEST_PRECEDENCE);

        // OrderedGatewayFilter 클래스에서 사용하는 메서드
        // ServerWebExchange 객체는 우리가 필요한 request와 response 를 얻어옴
        // GatewayFilterchain 객체는 다양한 필터들(pre, post..)을 연결시켜줌
//        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//            return this.delegate.filter(exchange, chain);
//        }

        return filter;
    }

    @Data // getter 함수를 만들어주기 위해 사용
    public static class Config {
        // Put the configuration properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
