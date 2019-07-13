package com.pupperflux.app.config;

import com.pupperflux.app.post.PostCreatedEvent;
import com.pupperflux.app.post.PostCreatedEventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class WebSocketConfig {

    @Bean
    public Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public HandlerMapping handlerMapping(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping() {
            {
                setUrlMap(Collections.singletonMap("/ws/posts", wsh));
                setOrder(10);
            }
        };
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public WebSocketHandler webSocketHandler(ObjectMapper objectMapper,
                                             PostCreatedEventPublisher eventPublisher) {

        Flux<PostCreatedEvent> publish = Flux.create(eventPublisher).share();

        return session -> {
            Flux<WebSocketMessage> messageFlux = publish
                    .map(event -> {
                        try {
                            return objectMapper.writeValueAsString(event.getSource());
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }).map(session::textMessage);

            return session.send(messageFlux);
        };
    }
}
