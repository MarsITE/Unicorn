package com.academy.workSearch.service.implementation;

import com.academy.workSearch.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private JedisPool jedisPool;

    @Override
    public boolean setValue(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
            return true;
        } catch (JedisException e) {
            return false;
        }

    }

    @Override
    public String getValue(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (JedisException e) {
            return "";
        }
    }
}
