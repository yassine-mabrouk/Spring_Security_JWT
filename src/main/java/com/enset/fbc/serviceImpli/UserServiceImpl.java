package com.enset.fbc.serviceImpli;

import com.enset.fbc.entities.RoleEntity;
import com.enset.fbc.entities.UserEntity;
import com.enset.fbc.repositories.RoleRepository;
import com.enset.fbc.repositories.UserRepository;
import com.enset.fbc.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository ;
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

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public RoleEntity createRole(RoleEntity role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
            UserEntity user = userRepository.findByEmail(email);
            RoleEntity role = roleRepository.findByRoleName(roleName);
            user.getRoles().add(role);
            userRepository.save(user);
    }

    @Override
    public UserEntity getUserById(Long id) {
        Optional<UserEntity> user =userRepository.findById(id);

        if (!user.isPresent())
            throw new RuntimeException("Element with id = " + id+ " is not found");
        return  userRepository.findById(id).get();
    }

    @Override
    public void deleteUser(Long id ) {
        Optional<UserEntity> user =userRepository.findById(id);

        if (!user.isPresent())
            throw new RuntimeException("User  with id = " + id+ " is not found");
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        UserEntity user =  userRepository.findByEmail(email) ;
        if (user==null ) throw  new UsernameNotFoundException("User not fount with this email"+ email);
        return user;
    }

    //    retrieve user from BD
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         UserEntity user =  userRepository.findByEmail(email) ;
         if (user==null ) throw  new UsernameNotFoundException("User not fount with this email"+ email);
        Collection <GrantedAuthority > authorities = new ArrayList<>( );
        user.getRoles().forEach(r -> {
            authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
        });
        return new User(user.getEmail(), user.getPassword() , authorities);
    }
}
