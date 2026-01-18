package jrbam.project.moviereservationsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${redis.ttl}")
    private Long timeToLive;

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(timeToLive))
                .disableCachingNullValues();

        // Custom TTLs for different cache names
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // Individual movie entries - longer TTL (1 hour)
        cacheConfigurations.put("movies", config.entryTtl(Duration.ofHours(1)));

        // Paginated results - shorter TTL (5 minutes)
        cacheConfigurations.put("movies_page", config.entryTtl(Duration.ofMinutes(5)));
        cacheConfigurations.put("movies_genre_page", config.entryTtl(Duration.ofMinutes(5)));
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
