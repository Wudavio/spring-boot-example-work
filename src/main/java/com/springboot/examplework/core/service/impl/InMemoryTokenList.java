package com.springboot.examplework.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.springboot.examplework.security.service.Tokenlist;

import java.util.*;

@Service
public class InMemoryTokenList implements Tokenlist {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String BLACKLIST_KEY = "blacklist";
    private static final String REFRESH_TOKEN_MAP_KEY = "refreshTokenMap";

    @Override
    public void addToBlacklist(String token) {
        redisTemplate.opsForSet().add(BLACKLIST_KEY, token);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return redisTemplate.opsForSet().isMember(BLACKLIST_KEY, token);
    }

    @Override
    public void addToRefreshTokenMap(String token, String refreshToken) {
        redisTemplate.opsForHash().putIfAbsent(REFRESH_TOKEN_MAP_KEY, refreshToken, new ArrayList<String>());
        List<String> tokens = (List<String>) redisTemplate.opsForHash().get(REFRESH_TOKEN_MAP_KEY, refreshToken);
        tokens.add(token);
        redisTemplate.opsForHash().put(REFRESH_TOKEN_MAP_KEY, refreshToken, tokens);
    }

    @Override
    public void addBlacklistToRefreshTokenMap(String refreshToken) {
        List<String> tokens = (List<String>) redisTemplate.opsForHash().get(REFRESH_TOKEN_MAP_KEY, refreshToken);
        if (tokens != null) {
            for (String token : tokens) {
                addToBlacklist(token);
            }
            tokens.clear();
            redisTemplate.opsForHash().put(REFRESH_TOKEN_MAP_KEY, refreshToken, tokens);
        }
    }
}
