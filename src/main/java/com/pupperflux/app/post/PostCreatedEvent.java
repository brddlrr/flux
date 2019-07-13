package com.pupperflux.app.post;

import org.springframework.context.ApplicationEvent;

public class PostCreatedEvent extends ApplicationEvent {

    public PostCreatedEvent(Post source) {
        super(source);
    }
}
