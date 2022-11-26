package com.enset.fbc.controllers;



import com.enset.fbc.entities.AddRoleToUserRequest;
import com.enset.fbc.entities.UserEntity;
import com.enset.fbc.repositories.UserRepository;
import com.enset.fbc.security.SecurityConstants;
import com.enset.fbc.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")  //localhost:8080/users
public class UserController {
    @Autowired
    UserService userService;
  @Autowired
    UserRepository userRepository;
    // get  a user
    @GetMapping("/{id}")
    public UserEntity getUser(@PathVariable Long id ){
          return  userService.getUserById(id);
    }
    @GetMapping
    public List<UserEntity> getAllUsers(){
        return  userService.getAllUsers();
    }

    @PostMapping
    public UserEntity creteUser(@RequestBody UserEntity user){
        UserEntity createUser =userService.createUser(user);
        return  createUser;
    }
     @PutMapping
     @PostAuthorize("hasAuthority('USER')")
     public String updateUser(){
        return  "updateUser was called ";
    }

     @DeleteMapping("/{id}")
     public String deleteUser(@PathVariable Long id){
            userService.deleteUser(id);
        return  "delteUser was called ";
    }

    @PostMapping ("addRoleToUser")
    @PostAuthorize("hasAuthority('ADMIN')")
    public  void addRoleToUser (@RequestBody AddRoleToUserRequest   request){
         userService.addRoleToUser(request.getEmail(), request.getRoleName());
    }
     @GetMapping("/refreshToken")
    public void generateNewToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
         // get the hearder
         String   refreshToken = request.getHeader(SecurityConstants.HEADER_STRING);
         if (refreshToken!=null  && refreshToken.startsWith(SecurityConstants.TOKEN_PREFIX)){
             try {
                 refreshToken = refreshToken.replace(SecurityConstants.TOKEN_PREFIX, "");
                 Claims claims = Jwts.parser()
                         .setSigningKey( SecurityConstants.TOKEN_SECRET )
                         .parseClaimsJws( refreshToken )
                         .getBody();
                 String username = claims.getSubject();
                 UserEntity user = userService.getUserByEmail(username);
                 // creer le acces token

                 String Accesstoken = Jwts.builder()
                         .setSubject(user.getEmail())
                         .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                         .setIssuer(request.getRequestURL().toString())
                         // set private claims
                         .claim("test", "My Private claim ")
                         .claim("roles" ,user.getRoles().stream().map(r-> r.getRoleName()).collect(Collectors.toList()) )
                         .signWith(SignatureAlgorithm.HS256, SecurityConstants.TOKEN_SECRET)
                         .compact();

                 Map<String, String> idTokens = new HashMap<String, String>();
                 idTokens.put("refreshToken", refreshToken);
                 idTokens.put("accessToken", Accesstoken);
                 // indiquer que le contenu est de type json
                 response.setContentType("application/json");
                 // envoyer les token dans le cord de reponse
                 new ObjectMapper().writeValue(response.getOutputStream(), idTokens);
             }catch (Exception e){
                 response.setHeader("error message ", e.getMessage());
                 response.sendError(HttpServletResponse.SC_FORBIDDEN);
                 throw e;
             }
         }else {
             throw new RuntimeException ("Refresh token required ");
         }
     }


    @GetMapping (path= "/profil")
    public UserEntity getProfil(Principal principal){
        return userService.getUserByEmail(principal.getName());
    }


}



