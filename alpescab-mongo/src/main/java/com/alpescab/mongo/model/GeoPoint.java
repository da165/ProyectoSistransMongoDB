package com.alpescab.mongo.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoPoint {
    private String name;
    private String address;
    private String city;
    private double latitude;
    private double longitude;
}
