package com.springboot.examplework.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.examplework.user.entity.UserPO;

public interface UserDAO extends JpaRepository<UserPO, Long> {
    UserPO findById(long id);
    UserPO findByAccount(String account);
    Boolean existsByAccount(String account);
}
