package com.execodex.sparrowair2.services;

import com.execodex.sparrowair2.entities.Message;
import com.execodex.sparrowair2.repositories.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Get all messages
    public Flux<Message> getAllMessages() {
        return messageRepository.findAll()
                .doOnError(e -> logger.error("Error retrieving all messages", e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving all messages", e);
                    return Flux.error(e);
                });
    }

    // Get message by id
    public Mono<Message> getMessageById(Integer id) {
        return messageRepository.findById(id)
                .doOnError(e -> logger.error("Error retrieving message with id: {}", id, e))
                .onErrorResume(e -> {
                    logger.error("Error retrieving message with id: {}", id, e);
                    return Mono.error(e);
                });
    }

    // Create a new message
    public Mono<Message> createMessage(Message message) {
        return messageRepository.save(message)
                .doOnSuccess(m -> logger.info("Created message with id: {}", m.getId()))
                .doOnError(e -> {
                    if (e instanceof DuplicateKeyException) {
                        logger.error("Duplicate key error when creating message with id: {}", message.getId(), e);
                    } else {
                        logger.error("Error creating message with email: {}", message.getEmail(), e);
                    }
                })
                .onErrorResume(e -> Mono.error(e));
    }

    // Update an existing message
    public Mono<Message> updateMessage(Integer id, Message message) {
        message.setId(id); // Ensure the ID is set correctly
        return messageRepository.save(message)
                .doOnSuccess(m -> logger.info("Updated message with id: {}", m.getId()))
                .doOnError(e -> logger.error("Error updating message with id: {}", id, e))
                .onErrorResume(e -> Mono.error(e));
    }

    // Delete a message
    public Mono<Void> deleteMessage(Integer id) {
        return messageRepository.findById(id)
                .flatMap(message -> messageRepository.delete(message)
                        .doOnSuccess(v -> logger.info("Deleted message with id: {}", id))
                        .doOnError(e -> logger.error("Error deleting message with id: {}", id, e))
                        .onErrorResume(e -> Mono.error(e))
                )
                .switchIfEmpty(Mono.empty());
    }
}
