package com.enset.fbc.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id ;
    private  String name ;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  //  For not serialize password in the moment get
    private  String  password;
      @ManyToMany (fetch = FetchType.EAGER)
     private List<RoleEntity>  roles = new ArrayList<>() ; // if I use EAGER we should init List

}
