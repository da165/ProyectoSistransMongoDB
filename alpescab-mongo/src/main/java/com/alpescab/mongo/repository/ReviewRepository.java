package com.alpescab.mongo.repository;

import com.alpescab.mongo.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByTargetUserId(String targetUserId);
}
