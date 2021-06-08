package com.academy.workSearch.service;

public interface RedisService {
    String KEY_REFRESH_TOKEN = "refresh_token";

    boolean setValue(String key, String value);

    String getValue(String key);
}
