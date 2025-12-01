package com.alpescab.mongo.repository;

import com.alpescab.mongo.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    java.util.List<Vehicle> findByDriverId(String driverId);
}
