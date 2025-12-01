package com.alpescab.mongo.repository;

import com.alpescab.mongo.model.ServiceUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceUserRepository extends MongoRepository<ServiceUser, String> {
}
