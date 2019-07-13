package com.pupperflux.app.config;

import com.pupperflux.app.post.PostHandler;
import com.pupperflux.app.util.CaseInsensitiveRequestPredicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableWebFlux
public class PostEndpointConfig {

    @Bean
    RouterFunction<ServerResponse> routes(PostHandler handler) {
        return route(ignoreCase(GET("/posts")), handler::getAll)
                .andRoute(ignoreCase(GET("/posts/{id}")), handler::getById)
                .andRoute(ignoreCase(POST("/posts")), handler::create)
                .andRoute(ignoreCase(PUT("/posts/{id}")), handler::updateById)
                .andRoute(ignoreCase(DELETE("/posts/{id}")), handler::deleteById);
    }

    private static RequestPredicate ignoreCase(RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }
}
