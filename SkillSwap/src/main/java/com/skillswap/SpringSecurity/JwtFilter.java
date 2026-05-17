package com.skillswap.SpringSecurity;

import com.skillswap.userservice.ServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ServiceImpl service;



    public String extractUserId(String token){

      Claims claims=Jwts.parserBuilder()
              .setSigningKey(jwtUtil.key())
              .build()
              .parseClaimsJws(token)
              .getBody();

      return claims.getSubject();


    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=null;
        String userid=null;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie: request.getCookies()){
                if(cookie.getName().equals("JWT")){
                    token=cookie.getValue();
                    break;
                }
            }
        }

       if(token==null){
           if(request.getHeader("Authorization")!=null) {
               String authToken = request.getHeader("Authorization");
               token = authToken.substring(7);
           }
       }

       if(token!=null){
           userid=extractUserId(token);
       }

       if(userid!=null && SecurityContextHolder.getContext().getAuthentication()==null){
           UserDetails userDetails = service.loadUserByUsername(userid);
           if (userDetails != null) {

               UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(authToken);
           }
       }

      filterChain.doFilter(request, response);
    }
}
