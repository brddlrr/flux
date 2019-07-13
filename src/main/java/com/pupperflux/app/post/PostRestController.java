package com.pupperflux.app.post;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController("/posts")
public class PostRestController {

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    private final PostService postService;

    @GetMapping
    public Publisher<Post> getAll() {
        return postService.all();
    }

    @GetMapping("/{id}")
    public Publisher<Post> getOne(@PathVariable String id) {
        return postService.get(id);
    }

    @PostMapping
    public Publisher<ResponseEntity<Post>> createPost(@RequestBody Post post) {
        return postService
                .create(post.getTextContent(), post.getAuthorId())
                .map(p -> ResponseEntity.created(URI.create("/posts/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build());
    }

    @PutMapping("/{id}")
    public Publisher<ResponseEntity<Post>> updatePost(@PathVariable String id, @RequestBody Post post) {
        return Mono
                .just(post)
                .flatMap(p -> postService.update(id, p.getTextContent()))
                .map(p -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .build());
    }

    @DeleteMapping("/{id}")
    public Publisher<Post> deletePost(@PathVariable String id) {
        return postService.delete(id);
    }

}
