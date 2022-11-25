package com.enset.fbc.service;


import com.enset.fbc.entities.RoleEntity;
import com.enset.fbc.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService  extends UserDetailsService {
    public UserEntity createUser(UserEntity userDto);
     UserEntity getUser(String email);
    List<UserEntity > getAllUsers ();
    public RoleEntity createRole ( RoleEntity role );
    public  void addRoleToUser (String email , String roleName );
    UserEntity getUserById (Long id );

}
