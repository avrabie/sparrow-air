package com.execodex.sparrowair2.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration for HTTP request logging.
 */
@Configuration
public class RequestLoggingConfig {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingConfig.class);

    /**
     * Creates a WebFilter that logs incoming HTTP requests.
     * 
     * @return A WebFilter that logs request details
     */
    @Bean
    public WebFilter requestLoggingFilter() {
        return (exchange, chain) -> {
            long startTime = System.currentTimeMillis();

            // Log the incoming request
            logger.info("Incoming Request: {} {} from {}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI(),
                    exchange.getRequest().getRemoteAddress());

            // Continue the filter chain and log the response time when complete
            return chain.filter(exchange)
                    .doOnSuccess(v -> {
                        long duration = System.currentTimeMillis() - startTime;
                        logger.info("Request completed: {} {} - Status: {} - Duration: {}ms",
                                exchange.getRequest().getMethod(),
                                exchange.getRequest().getURI(),
                                exchange.getResponse().getStatusCode(),
                                duration);
                    })
                    .doOnError(error -> {
                        long duration = System.currentTimeMillis() - startTime;
                        logger.error("Request failed: {} {} - Duration: {}ms - Error: {}",
                                exchange.getRequest().getMethod(),
                                exchange.getRequest().getURI(),
                                duration,
                                error.getMessage());
                    });
        };
    }
}
