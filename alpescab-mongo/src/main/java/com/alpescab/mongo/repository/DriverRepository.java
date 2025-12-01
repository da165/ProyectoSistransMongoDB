package com.alpescab.mongo.repository;

import com.alpescab.mongo.model.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DriverRepository extends MongoRepository<Driver, String> {
}
