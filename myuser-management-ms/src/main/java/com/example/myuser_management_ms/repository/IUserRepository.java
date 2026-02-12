package com.example.myuser_management_ms.repository;

import com.example.myuser_management_ms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUserName(String userName);

    void deleteByUserName(String userName);
}
