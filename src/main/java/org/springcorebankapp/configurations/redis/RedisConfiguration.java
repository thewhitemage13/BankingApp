package org.springcorebankapp.configurations.redis;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Configuration class for Redis-based caching.
 * <p>
 * This class defines the caching configuration for the application using Redis.
 * It customizes key and value serialization strategies and sets a default time-to-live (TTL) for cache entries.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *     <li>Integration with Redis for caching data.</li>
 *     <li>Custom serialization of cache keys and values using {@link StringRedisSerializer}
 *     and {@link GenericJackson2JsonRedisSerializer}.</li>
 *     <li>Default cache entry time-to-live (TTL) of 10 seconds.</li>
 * </ul>
 *
 * @see RedisCacheManager
 * @see RedisConnectionFactory
 * @see org.springframework.cache.annotation.Cacheable
 * @see org.springframework.cache.annotation.CacheEvict
 * @see org.springframework.cache.annotation.Caching
 * @see org.springframework.cache.annotation.EnableCaching
 *
 * @author Mukhammed Lolo
 * @version 1.0.0
 */
@Configuration
public class RedisConfiguration {

    /**
     * Configures and returns a {@link CacheManager} bean for managing caches in Redis.
     * <p>
     * The cache manager uses a custom Redis cache configuration that includes:
     * <ul>
     *     <li>A time-to-live (TTL) of 10 seconds for cache entries.</li>
     *     <li>Key serialization using {@link StringRedisSerializer}.</li>
     *     <li>Value serialization using {@link GenericJackson2JsonRedisSerializer}.</li>
     * </ul>
     *
     * @param redisConnectionFactory the factory for creating Redis connections
     * @return a configured {@link CacheManager} instance
     */
    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory
    ) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}
