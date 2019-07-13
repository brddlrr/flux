package com.pupperflux.app.post;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "authorId"})
@Document("posts")
public class Post {

    @Id
    private String id;
    private String authorId;
    private String textContent;
}
