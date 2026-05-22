package com.innowise.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * Redis cache configuration.
 * Configures cache TTL and JSON serialization.
 */
@Configuration
@Profile("!test")
public class RedisConfig {
}
