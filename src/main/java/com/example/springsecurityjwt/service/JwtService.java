package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.entity.User;
import com.example.springsecurityjwt.entity.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String SECRET_KEY = "6A9560A8F1DADF0E61911D7DBC17F1BAAABD3466E1DB1C6B57756F702CA6DA5A";

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwtToken).getBody();
    }


    public <C> C extractClaim(String jwtToken, Function<Claims, C> claimsFunction) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsFunction.apply(claims);
    }

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }


    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }


    public String generateToken(Map<String, Object> extraClaims, User user) {
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(role -> role.equals(Role.ADMIN.name()) || role.equals(Role.USER.name())).collect(Collectors.toList());

        return Jwts.builder().setClaims(extraClaims).setSubject(user.getUsername()).claim("roles", roles)//Add roles as a claim
                .setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 10 * 1000 * 60 * 24)).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public String generateEndlessToken(User user) {
        return generateEndlessToken(new HashMap<>(), user);
    }

    public String generateEndlessToken(Map<String, Object> extraClaims, User user) {
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).filter(role -> role.equals(Role.ADMIN.name()) || role.equals(Role.USER.name())).collect(Collectors.toList());

        return Jwts.builder().setClaims(extraClaims).setSubject(user.getUsername()).claim("roles", roles)//Add roles as a claim
                .setIssuedAt(new Date(System.currentTimeMillis())).signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public boolean isTokenValid(String jwtToken, User user) {
        final String username = extractUsername(jwtToken);
        return (username.equals(user.getUsername()) && !isTokenExpired(jwtToken));
    }


    public Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public boolean isTokenExpired(String jwtToken) {
        if (extractExpiration(jwtToken) == null) {
            return false; //which mean it is endless token
        }
        return extractExpiration(jwtToken).before(new Date(System.currentTimeMillis()));
    }


}
