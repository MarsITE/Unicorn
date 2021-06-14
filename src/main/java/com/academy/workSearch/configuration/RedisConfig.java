package com.academy.workSearch.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;

@Component
public class RedisConfig {

    @Autowired
    private Environment environment;

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(new JedisPoolConfig(), environment.getProperty("HOST"), Integer.parseInt(Objects.requireNonNull(environment.getProperty("PORT"))));
    }
}
