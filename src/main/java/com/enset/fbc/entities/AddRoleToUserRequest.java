package com.enset.fbc.entities;

import lombok.Data;

@Data
public class AddRoleToUserRequest {
    private  String email ;
    private String roleName ;
}
