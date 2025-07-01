package com.cropdeal.userservice.service.implementation;


import com.cropdeal.userservice.exception.TokenValidationException;
import com.cropdeal.userservice.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JWTServiceImp implements JWTService {

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateToken(UserDetails userDetails)
    {
        String role= userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("Role_User");
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("Role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256).compact();
    }

    @Override
    public String generateRefreshToken(HashMap<String, Object> extraClaims, UserDetails userDetails) {
        String role= userDetails.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse("Role_User");
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .claim("Role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*5))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256).compact();

    }

    private Key getSiginKey() {
        byte[] key = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(key);

    }
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolvers)
    {
        final Claims claims = extractAllClaim(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
    }





    private boolean isTokenExpired(String token) {

        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String extractUserName(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            throw new TokenValidationException("Invalid token: " + e.getMessage());
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUserName(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            throw new TokenValidationException("Token validation failed: " + e.getMessage());
        }
    }

}
