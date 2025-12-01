package com.alpescab.mongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "availabilities")
public class Availability {
    @Id
    private String id;
    private String driverId;
    private String vehicleId;
    private String serviceType;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
