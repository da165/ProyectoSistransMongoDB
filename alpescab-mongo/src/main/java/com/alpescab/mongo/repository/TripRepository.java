package com.alpescab.mongo.repository;

import com.alpescab.mongo.model.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends MongoRepository<Trip, String> {
    List<Trip> findByServiceUserId(String serviceUserId);
    List<Trip> findByDriverId(String driverId);
    List<Trip> findByStartTimeBetweenAndStartPoint_City(LocalDateTime from, LocalDateTime to, String city);
    long countByDriverId(String driverId);
}
