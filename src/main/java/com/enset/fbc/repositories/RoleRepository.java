package com.enset.fbc.repositories;

import com.enset.fbc.entities.RoleEntity;
import com.enset.fbc.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RepositoryRestResource
public interface RoleRepository extends JpaRepository<RoleEntity , Long > {
    public     RoleEntity findByRoleName (String roleName );
}
