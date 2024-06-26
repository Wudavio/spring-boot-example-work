package com.springboot.examplework.security.utils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.examplework.core.dto.AjaxDTO;
import com.springboot.examplework.core.service.impl.InMemoryTokenList;
import com.springboot.examplework.security.service.MyUserDetailsService;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthenticationFilter implements Filter {
    private final static Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private InMemoryTokenList inMemoryTokenBlacklist;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final String HEADER_AUTH = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader(HEADER_AUTH);

        try {
            if (ObjectUtils.isNotEmpty(authHeader)) {
                String accessToken = authHeader.replace("Bearer ", "");
                if (inMemoryTokenBlacklist.isBlacklisted(accessToken)) {
                    sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Token失效, 請重新登入");
                    return;
                }

                Map<String, String> parseTokenMap = jwtUtil.parseToken(accessToken);
                String account = parseTokenMap.get("account");
                String tableName = parseTokenMap.get("table");
                String type = parseTokenMap.get("type");

                if ("refresh".equals(type)) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "請勿將Refresh Token當作Access Token使用");
                    return;
                }

                MyUserDetails myUser = myUserDetailsService.loadUserByUsername(account + ":_:" + tableName);

                if (myUser == null) {
                    log.error("User not found");
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token失效, 找不到使用者, 請重新登入");
                    return;
                }

                if (!jwtUtil.isTokenValid(accessToken, myUser)) {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token失效, 請重新登入");
                    return;
                }

                Authentication authentication = new UsernamePasswordAuthenticationToken(myUser, myUser.getPassword(), myUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            log.error("Error: ", e);
        } catch (NullPointerException e) {
            log.error("Error: ", e);
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        AjaxDTO errorResponse = new AjaxDTO();
        errorResponse.setMessage(message);
        errorResponse.setStatusFail();
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}