package com.springboot.examplework.security.service;

public interface Tokenlist {
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);

    void addToRefreshTokenMap(String token, String refreshToken);

    void addBlacklistToRefreshTokenMap(String refreshToken);
}
