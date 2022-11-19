package com.enset.fbc.serviceImpli;

import com.enset.fbc.entities.UserEntity;
import com.enset.fbc.repositories.UserRepository;
import com.enset.fbc.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;


    @Override
    public UserEntity createUser(UserEntity userDto) {
        UserEntity checkUser = userRepository.findByEmail(userDto.getEmail());
        // verifier existance of the user in BD
        if (checkUser!=null) throw  new RuntimeException("User existe !!!");
         UserEntity saved = userRepository.save(userDto);
          return saved;
    }

    @Override
    public UserEntity getUser(String email) {
        UserEntity userDto = userRepository.findByEmail(email);
        return userDto;
    }
}
