package com.instaticket.dao;

import java.util.List;

import com.instaticket.repository.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {
    User findByUsername(String username);

    List<User> findByRole(String string);

    List<User> findByRoleAndStatus(String string, boolean b);

    List<User> findAllByOrderByUserIdDesc();

}
