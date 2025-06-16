package com.execodex.sparrowair2.routes;

import com.execodex.sparrowair2.handlers.MessageHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class MessageRoutes {

    private final MessageHandler messageHandler;

    public MessageRoutes(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/messages",
                    method = RequestMethod.GET,
                    beanClass = MessageHandler.class,
                    beanMethod = "getAllMessages",
                    operation = @Operation(
                            operationId = "getAllMessages",
                            summary = "Get all messages",
                            description = "Returns a list of all messages",
                            tags = {"Messages 📨"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/messages/{id}",
                    method = RequestMethod.GET,
                    beanClass = MessageHandler.class,
                    beanMethod = "getMessageById",
                    operation = @Operation(
                            operationId = "getMessageById",
                            summary = "Get message by id",
                            description = "Returns a message by id",
                            tags = {"Messages 📨"},
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(name = "id", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH, required = true,
                                            content = @Content(schema = @Schema(type = "integer")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Successful operation",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/messages",
                    method = RequestMethod.POST,
                    beanClass = MessageHandler.class,
                    beanMethod = "createMessage",
                    operation = @Operation(
                            operationId = "createMessage",
                            summary = "Create a new message",
                            description = "Creates a new message",
                            tags = {"Messages 📨"},
                            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                    description = "Message object to be created",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = com.execodex.sparrowair2.entities.Message.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Message created successfully",
                                            content = @Content(mediaType = "application/json")
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/messages/{id}",
                    method = RequestMethod.DELETE,
                    beanClass = MessageHandler.class,
                    beanMethod = "deleteMessage",
                    operation = @Operation(
                            operationId = "deleteMessage",
                            summary = "Delete a message",
                            description = "Deletes a message by id",
                            tags = {"Messages 📨"},
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(name = "id", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH, required = true,
                                            content = @Content(schema = @Schema(type = "integer")))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Message deleted successfully"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> messageRoutesFunction() {
        return RouterFunctions.route()
                .path("/messages", builder -> builder
                        // GET /messages - Get all messages
                        .GET("", accept(MediaType.APPLICATION_JSON), messageHandler::getAllMessages)
                        // GET /messages/{id} - Get message by id
                        .GET("/{id}", accept(MediaType.APPLICATION_JSON), messageHandler::getMessageById)
                        // POST /messages - Create a new message
                        .POST("", accept(MediaType.APPLICATION_JSON), messageHandler::createMessage)
                        // DELETE /messages/{id} - Delete a message
                        .DELETE("/{id}", messageHandler::deleteMessage)
                )
                .build();
    }
}
