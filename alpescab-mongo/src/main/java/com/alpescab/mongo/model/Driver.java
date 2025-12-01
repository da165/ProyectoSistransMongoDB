package com.alpescab.mongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "drivers")
public class Driver {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private String nationalId;
    private double ratingAverage;
    private long ratingCount;
}
