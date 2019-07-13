package com.pupperflux.app.post;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.*;
import java.util.function.Consumer;

@Component
public class PostCreatedEventPublisher implements
        ApplicationListener<PostCreatedEvent>,
        Consumer<FluxSink<PostCreatedEvent>> {

    private final Executor executor;
    private final BlockingQueue<PostCreatedEvent> queue = new LinkedBlockingQueue<>();

    public PostCreatedEventPublisher(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void accept(FluxSink<PostCreatedEvent> sink) {
        executor.execute(() -> {
            while (true) {

                try {
                    PostCreatedEvent event = queue.take();
                    sink.next(event);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void onApplicationEvent(PostCreatedEvent event) {
        queue.offer(event);
    }
}
