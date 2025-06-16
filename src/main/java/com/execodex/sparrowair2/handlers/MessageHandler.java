package com.execodex.sparrowair2.handlers;

import com.execodex.sparrowair2.entities.Message;
import com.execodex.sparrowair2.services.MessageService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class MessageHandler {

    private final MessageService messageService;

    public MessageHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    // Get all messages
    public Mono<ServerResponse> getAllMessages(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(messageService.getAllMessages(), Message.class)
                .onErrorResume(this::handleError);
    }

    // Get message by id
    public Mono<ServerResponse> getMessageById(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return messageService.getMessageById(id)
                .flatMap(message -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(message))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Create a new message
    public Mono<ServerResponse> createMessage(ServerRequest request) {
        // Get client IP address from the request
        String ipAddress = request.exchange().getRequest().getRemoteAddress().getAddress().getHostAddress();

        return request.bodyToMono(Message.class)
                .map(message -> {
                    message.setIpAddress(ipAddress);
                    return message;
                })
                .flatMap(messageService::createMessage)
                .flatMap(message -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(message))
                .onErrorResume(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return ServerResponse
                                .status(HttpStatus.CONFLICT)
                                .bodyValue("Message with id already exists");
                    }
                    return handleError(e);
                });
    }

    // Delete a message
    public Mono<ServerResponse> deleteMessage(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return messageService.getMessageById(id)
                .flatMap(message -> messageService.deleteMessage(id)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(this::handleError);
    }

    // Common error handler
    private Mono<ServerResponse> handleError(Throwable error) {
        return ServerResponse
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue("An error in MessageHandler occurred: " + error.getMessage());
    }
}
