package com.alpescab.mongo.repository;

import com.alpescab.mongo.model.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AvailabilityRepository extends MongoRepository<Availability, String> {
    List<Availability> findByDriverId(String driverId);
    List<Availability> findByServiceTypeAndDayOfWeek(String serviceType, String dayOfWeek);
}
