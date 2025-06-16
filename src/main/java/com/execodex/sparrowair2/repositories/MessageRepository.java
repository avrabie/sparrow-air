package com.execodex.sparrowair2.repositories;

import com.execodex.sparrowair2.entities.Message;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ReactiveCrudRepository<Message, Integer> {
    // The primary key of Message is id, which is an Integer
    // ReactiveCrudRepository provides basic CRUD operations with reactive return types
}
