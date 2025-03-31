package org.autovill.officeapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${spring.data.redis.host:localhost}")
    private String host;
    @Value("${spring.data.redis.port:6379}")
    private Integer port;
    @Value("${spring.data.redis.password:}")
    private String password;
    @Value("${spring.data.redis.ssl:false}")
    private Boolean ssl;

    @Bean
    LettuceConnectionFactory customConnectionFactory() throws IOException {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        configuration.setPassword(password);

        LettuceClientConfigurationBuilder clientConfigurationBuilder = LettuceClientConfiguration.builder();
        if (ssl) {
            clientConfigurationBuilder.useSsl()
                    .disablePeerVerification().and();
        }
        LettuceClientConfiguration clientConfig = clientConfigurationBuilder
                .build();
        return new LettuceConnectionFactory(configuration, clientConfig);
    }

    @Bean
    ReactiveRedisTemplate<String, UUID> redisTemplate(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<UUID> valueSerializer = new Jackson2JsonRedisSerializer<>(UUID.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, UUID> builder = RedisSerializationContext
                .newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, UUID> context = builder
                .value(valueSerializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

//    @Bean
//    ReactiveRedisTemplate<String, UUID> mailCacheRedisTemplate(ReactiveRedisConnectionFactory factory) {
//        Jackson2JsonRedisSerializer<UUID> valueSerializer = new Jackson2JsonRedisSerializer<>(UUID.class);
//
//        RedisSerializationContext.RedisSerializationContextBuilder<String, UUID> builder = RedisSerializationContext
//                .newSerializationContext(new StringRedisSerializer());
//        RedisSerializationContext<String, UUID> context = builder
//                .value(valueSerializer)
//                .build();
//
//        return new ReactiveRedisTemplate<>(factory, context);
//    }
}
