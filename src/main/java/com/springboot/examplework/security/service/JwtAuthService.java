package com.springboot.examplework.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.springboot.examplework.security.utils.JwtUtil;
import com.springboot.examplework.security.utils.MyUserDetails;

import java.util.Map;

@Service
public class JwtAuthService {
    private final static Logger log = LoggerFactory.getLogger(JwtAuthService.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public String auth(String account, String password, String tableName){
        String token = jwtUtil.generateToken(account, tableName);
        return token;
    }

    public String refreshAuth(String account, String password, String tableName){
        String token = jwtUtil.generateRefreshToken(account, tableName);
        return token;
    }

    public boolean isValidToken(String token){
        Map<String, String> parseTokenMap = jwtUtil.parseToken(token);
        if(parseTokenMap == null){
            return false;
        }
        String account = parseTokenMap.get("account");
        String tableName = parseTokenMap.get("table");
        MyUserDetails myUser = myUserDetailsService.loadUserByUsername(account+":_:"+tableName);
        return jwtUtil.isTokenValid(token, myUser);
    }

    public boolean isValidRefreshToken(String token){
        Map<String, String> parseTokenMap = jwtUtil.parseToken(token);

        // 如果parseTokenMap為null，則代表token無效
        if(parseTokenMap == null){
            return false;
        }

        String account = parseTokenMap.get("account");
        String tableName = parseTokenMap.get("table");
        String type = parseTokenMap.get("type");
        MyUserDetails myUser = myUserDetailsService.loadUserByUsername(account+":_:"+tableName);
        boolean isTokenValid = jwtUtil.isTokenValid(token, myUser);
        if(isTokenValid && "refresh".equals(type)){
            return true;
        }
        return false;
    }

    public String refreshToken(String token){
        Map<String, String> parseTokenMap = jwtUtil.parseToken(token);
        String account = parseTokenMap.get("account");
        String tableName = parseTokenMap.get("table");
        return jwtUtil.generateToken(account, tableName);
    }
}