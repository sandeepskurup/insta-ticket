package com.instaticket.service;

import java.util.List;

import com.instaticket.bean.UserDTO;
import com.instaticket.repository.User;

public interface UserService {

    User save(UserDTO user);

    List<User> findAll();

    void delete(int id);

    User findOne(String username);

    User findById(int id);

    User update(UserDTO userDto);

    List<User> getDistributors();

    List<User> findOnlyUsers();

    User toggleStatus(int id);

    Boolean changePassword(UserDTO userDto, String name);

    Boolean resetPassword(int id);


}
