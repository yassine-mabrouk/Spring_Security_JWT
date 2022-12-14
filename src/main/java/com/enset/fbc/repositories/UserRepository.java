package com.enset.fbc.repositories;

import com.enset.fbc.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    public  UserEntity findByEmail(String email);
    @Override
    Optional<UserEntity> findById(Long aLong);
}
