package com.springboot.examplework.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private static final String ISS = "ExampleWorkISS";
    private static final String SECRET = "AlohomoraIsASpellUsedToOpenDoors";
    private static final int EXPIRE_TIME = 7;
    private static final int REFRESH_EXPIRE_TIME = 14;
    public static String generateToken(String account, String tableName) {
        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.DATE, EXPIRE_TIME);
        Claims claims = Jwts.claims();
        claims.put("table", tableName);
        claims.put("type", "access");
        claims.setSubject(account);
        claims.setExpiration(exp.getTime());
        claims.setIssuer(ISS);
        Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact(); // 將 JwtBuilder 構建的 JWT 物件，壓縮為一個字串的形式
    }

    public String generateRefreshToken(String account, String tableName) {
        Calendar exp = Calendar.getInstance();
        exp.add(Calendar.DATE, REFRESH_EXPIRE_TIME);
        Claims claims = Jwts.claims();
        claims.put("table", tableName);
        claims.put("type", "refresh");
        claims.setSubject(account);
        claims.setExpiration(exp.getTime());
        claims.setIssuer(ISS);
        Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact(); // 將 JwtBuilder 構建的 JWT 物件，壓縮為一個字串的形式
    }

    public Map<String, String> parseToken(String token){
        Key secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());

        Claims claims = extractAllClaims(token);

        if(claims == null){
            return null;
        }

        String account = claims.getSubject();
        String tableNmae = claims.get("table", String.class);
        String type = claims.get("type", String.class);
        Map<String, String> claimsMap = new HashMap<>();
        claimsMap.put("account", account);
        claimsMap.put("table", tableNmae);
        claimsMap.put("type", type);

        return claimsMap;
    }

    /**
     * 驗證Token有效性，比對JWT和UserDetails的account(Email)是否相同
     * @return 有效為True，反之False
     */
    public boolean isTokenValid(String token, MyUserDetails userDetails) {
        final String account = extractUsername(token);
        return (account.equals(userDetails.getAccount())) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        }catch (ExpiredJwtException e){
            return e.getClaims().getSubject();
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        final Date expirationDate = extractExpiration(token);
        return expirationDate != null && expirationDate.before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 獲取令牌中所有的聲明
     * @return 令牌中所有的聲明
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        } catch (Exception e){
            log.error("JWT Token解析失敗");
            return null;
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = SECRET.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}