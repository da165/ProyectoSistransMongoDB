package com.alpescab.mongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "vehicles")
public class Vehicle {
    @Id
    private String id;
    private String driverId;
    private String type;
    private String brand;
    private String model;
    private String color;
    private String plate;
    private String plateCity;
    private int capacity;
    private String level;
}
