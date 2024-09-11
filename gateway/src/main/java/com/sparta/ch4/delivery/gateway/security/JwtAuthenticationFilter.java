//package com.sparta.ch4.delivery.gateway.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;
//import java.net.http.HttpHeaders;
//import java.security.Key;
//import java.util.Base64;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
// TODO: JWT 인증 구현
//@Component
//public class JwtAuthenticationFilter {
//
//    @Value("${jwt.secret.key}")
//    private String secretKey;
//
//    private Key key;
//
//    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//    @PostConstruct
//    public void init() {
//        byte[] bytes = Base64.getDecoder().decode(secretKey);
//        key = Keys.hmacShaKeyFor(bytes);
//    }
//
//    public String getJwtFromHeader(HttpHeaders request) {
//        org.springframework.http.HttpHeaders headers = exchange.getRequest().getHeaders();
//        String authHeader = headers.getFirst(org.springframework.http.HttpHeaders.AUTHORIZATION);
//        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//        if (StringUtils.hasText(bearerToken)) {
//            String decodedToken = URLDecoder.decode(bearerToken, StandardCharsets.UTF_8);
//            if (decodedToken.startsWith(BEARER_PREFIX)) {
//                return decodedToken.substring(7);
//            }
//        }
//        return "";
//    }
//
//    public boolean validateToken(String token) {
//        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//    }
//
//}
