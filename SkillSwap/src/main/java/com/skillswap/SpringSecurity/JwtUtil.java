package com.skillswap.SpringSecurity;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Time;
import java.util.Date;

@Component
public class JwtUtil {


    private String key="bgdfsgsghg78fhfh8h7df8hdfsbhdfbgfh8934";
    private long time=1000*60*60;

    public Key key(){

        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

    }

    public String generateToken(String userid){
        return Jwts.builder()
                .setSubject(userid)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

    }


}
