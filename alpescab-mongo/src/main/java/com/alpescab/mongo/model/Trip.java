package com.alpescab.mongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "trips")
public class Trip {
    @Id
    private String id;
    private String serviceUserId;
    private String driverId;
    private String vehicleId;
    private String serviceType;
    private String level;
    private GeoPoint startPoint;
    private List<GeoPoint> endPoints;
    private double distanceKm;
    private double price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long durationSeconds;
    private String status;
}
