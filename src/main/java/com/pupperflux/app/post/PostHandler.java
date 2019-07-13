package com.pupperflux.app.post;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class PostHandler {

    private final PostService postService;

    public PostHandler(PostService postService) {
        this.postService = postService;
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return readResponse(postService.get(id(request)));
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return readResponse(postService.all());
    }

    public Mono<ServerResponse> create(ServerRequest request) {

        Mono<Post> post = request
                .bodyToMono(Post.class)
                .flatMap(toCreate -> postService.create(toCreate.getTextContent(), toCreate.getAuthorId()));

        return writeResponse(post);
    }

    public Mono<ServerResponse> updateById(ServerRequest request) {

        Mono<Post> post = request
                .bodyToMono(Post.class)
                .flatMap(toUpdate -> postService.update(id(request), toUpdate.getTextContent()));

        return writeResponse(post);
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        return readResponse(postService.delete(id(request)));
    }

    private Mono<ServerResponse> readResponse(Publisher<Post> posts) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(posts, Post.class);
    }

    private Mono<ServerResponse> writeResponse(Publisher<Post> posts) {
        return Mono
                .from(posts)
                .flatMap(post -> ServerResponse
                        .created(URI.create("/posts" + post.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build());
    }

    private String id(ServerRequest request) {
        return request.pathVariable("id");
    }
}
