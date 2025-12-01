package com.alpescab.mongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String tripId;
    private String authorUserId;
    private String targetUserId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
