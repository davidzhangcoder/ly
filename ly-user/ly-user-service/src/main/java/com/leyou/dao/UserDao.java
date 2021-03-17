package com.leyou.dao;

import com.leyou.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


public interface UserDao extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    public User findUserByUsername(String username);
}
