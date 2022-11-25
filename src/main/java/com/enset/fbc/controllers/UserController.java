package com.enset.fbc.controllers;



import com.enset.fbc.entities.UserEntity;
import com.enset.fbc.repositories.UserRepository;
import com.enset.fbc.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
     public String updateUser(){
        return  "updateUser was called ";
    }
     @DeleteMapping
     public String deleteUser(){
        return  "delteUser was called ";
    }

}

