package com.enset.fbc.service;


import com.enset.fbc.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  extends UserDetailsService {
    public UserEntity createUser(UserEntity userDto);

    UserEntity getUser(String email);

}
