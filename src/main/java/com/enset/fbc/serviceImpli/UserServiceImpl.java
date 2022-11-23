package com.enset.fbc.serviceImpli;

import com.enset.fbc.entities.UserEntity;
import com.enset.fbc.repositories.UserRepository;
import com.enset.fbc.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserEntity createUser(UserEntity userDto) {
        UserEntity checkUser = userRepository.findByEmail(userDto.getEmail());
        // verifier existance of the user in BD
        if (checkUser!=null) throw  new RuntimeException("User existe !!!");
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
         UserEntity saved = userRepository.save(userDto);
          return saved;
    }
    @Override
    public UserEntity getUser(String email) {
        UserEntity userDto = userRepository.findByEmail(email);
        return userDto;
    }
//    retrieve user from BD
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         UserEntity user =  userRepository.findByEmail(email) ;
         if (user==null ) throw  new UsernameNotFoundException("User not fount with this email"+ email);
        return new User(user.getEmail(), user.getPassword() , new ArrayList<>());
    }
}
