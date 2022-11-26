package com.enset.fbc.security;

import com.enset.fbc.entities.UserEntity;
import com.enset.fbc.request.UserLoginRequest;
import com.enset.fbc.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthentifiactionFilrer   extends UsernamePasswordAuthenticationFilter {

      private   final AuthenticationManager authenticationManager ;

    public AuthentifiactionFilrer( AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;
    }

//    when I sent request post/login , this function will be executed
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            UserLoginRequest connectedUser = new ObjectMapper().readValue(request.getInputStream(),UserLoginRequest.class);
              return  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(connectedUser.getEmail(),connectedUser.getPassword(),new ArrayList<>()));
        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException(e.getMessage());
        }
    }
    ///if the user is exist in the BD this function was executed
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
         // make token ( JWT ) and delevred to the client token= string encoded (header.payload.signature)
          String Accesstoken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                 .setIssuer(request.getRequestURL().toString())
                  // set private claims
                  .claim("test", "My Private claim ")
                  .claim("roles" , user.getAuthorities().stream().map(g-> g.getAuthority()).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.TOKEN_SECRET)
                .compact();
        String RefreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .setIssuer(request.getRequestURL().toString())
                // set private claims
                .claim("test", "My Private claim ")
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.TOKEN_SECRET)
                .compact();


        response.setContentType("application/json");
        response.addHeader("Username", user.getUsername());
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+ Accesstoken);
        Map<String , String > idTokens = new HashMap<>();
        idTokens.put("Accesstoken", Accesstoken);
        idTokens.put("RefreshToken", RefreshToken);

        new ObjectMapper().writeValue(response.getOutputStream(),idTokens);

        System.out.println(response);
    }
}
