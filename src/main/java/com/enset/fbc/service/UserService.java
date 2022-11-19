package com.enset.fbc.service;


import com.enset.fbc.entities.UserEntity;

public interface UserService {
    public UserEntity createUser(UserEntity userDto);

    UserEntity getUser(String email);


}
