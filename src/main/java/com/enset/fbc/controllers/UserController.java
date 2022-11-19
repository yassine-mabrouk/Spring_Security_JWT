package com.enset.fbc.controllers;



import com.enset.fbc.entities.UserEntity;
import com.enset.fbc.repositories.UserRepository;
import com.enset.fbc.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")  //localhost:8080/users
public class UserController {
    @Autowired
    UserService userService;
  @Autowired
    UserRepository userRepository;
    // recuperer un user
    @GetMapping("/{id}")
    public UserEntity getUser(@PathVariable Long id ){
        Optional<UserEntity> user =userRepository.findById(id);

        if (!user.isPresent())
            throw new RuntimeException("Element with id = " + id+ " is not found");
     return  userRepository.findById(id).get();
    }

    @PostMapping
    public UserEntity creteUser(@RequestBody UserEntity user){
        UserEntity createUser =userService.createUser(user);
        return  createUser;
    }
     @PutMapping
     public String updateUser(){
        return  "updateUser was called ";
    }
     @DeleteMapping
     public String deleteUser(){
        return  "delteUser was called ";
    }
    @GetMapping
    public  String getDta(){
        return "connected data ";
    }

}

