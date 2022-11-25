package com.enset.fbc;

import com.enset.fbc.entities.RoleEntity;
import com.enset.fbc.entities.UserEntity;
import com.enset.fbc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@Configuration
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Autowired
    UserService userService;
    @Override
    public void run(String... args) throws Exception {
        userService.createRole(new RoleEntity(null, "USER"));
        userService.createRole(new RoleEntity(null, "ADMIN"));
        userService.createRole(new RoleEntity(null, "MANAGER"));
         userService.createUser(new UserEntity(null, "user" , "user1@gmail.com", "1234", null ));
        userService.createUser(new UserEntity(null, "admin" , "admin@gmail.com", "1234", null ));
        userService.createUser(new UserEntity(null, "superAdmin" , "superAdmin@gmail.com", "1234", null ));

        userService.addRoleToUser("user1@gmail.com", "USER");
        userService.addRoleToUser("admin@gmail.com", "ADMIN");
        userService.addRoleToUser("superAdmin@gmail.com", "USER");
        userService.addRoleToUser("superAdmin@gmail.com", "ADMIN");
        userService.addRoleToUser("superAdmin@gmail.com", "MANAGER");
    }
}
