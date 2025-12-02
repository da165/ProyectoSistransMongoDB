package com.alpescab.mongo.dto;

import com.alpescab.mongo.model.GeoPoint;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class RequestTripDTO {
    private String serviceUserId;
    private String serviceType;
    private String level;
    private GeoPoint startPoint;
    private java.util.List<GeoPoint> endPoints;
    private double distanceKm;
    private double pricePerKm;
}
