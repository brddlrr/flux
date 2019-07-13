package com.pupperflux.app.post;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {

    private final ApplicationEventPublisher publisher;
    private final PostRepository postRepository;

    public PostService(ApplicationEventPublisher publisher, PostRepository postRepository) {
        this.publisher = publisher;
        this.postRepository = postRepository;
    }

    public Flux<Post> all() {
        return postRepository.findAll();
    }

    public Mono<Post> get(String id) {
        return postRepository.findById(id);
    }

    public Mono<Post> update(String id, String text) {
        return postRepository
                .findById(id)
                .map(post -> new Post(post.getId(), post.getAuthorId(), text))
                .flatMap(postRepository::save);
    }

    public Mono<Post> delete(String id) {
        return postRepository
                .findById(id)
                .flatMap(post -> postRepository.deleteById(post.getId()).thenReturn(post));
    }

    public Mono<Post> create(String text, String authorId) {
        return postRepository
                .save(new Post(null, authorId, text))
                .doOnSuccess(post -> publisher.publishEvent(new PostCreatedEvent(post)));
    }
}
