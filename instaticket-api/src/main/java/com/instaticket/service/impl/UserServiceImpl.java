package com.instaticket.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.instaticket.bean.UserDTO;
import com.instaticket.dao.UserDao;
import com.instaticket.repository.User;
import com.instaticket.service.UserService;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Value("${default.password}")
    private String defaultPassword;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	User user = userDao.findByUsername(username);
	if (user == null) {
	    throw new UsernameNotFoundException("Invalid username or password");
	}
	return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
		getAuthority(user.getRole()));
    }

    private List<SimpleGrantedAuthority> getAuthority(String role) {
	return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    @Override
    public List<User> findAll() {
	List<User> list = new ArrayList<>();
	userDao.findAllByOrderByUserIdDesc().iterator().forEachRemaining(list::add);
	return list;
    }

    @Override
    public void delete(int id) {
	userDao.deleteById(id);

    }

    @Override
    public User findOne(String username) {
	return userDao.findByUsername(username);
    }

    @Override
    public User findById(int id) {
	Optional<User> optionalUser = userDao.findById(id);
	return optionalUser.isPresent() ? optionalUser.get() : null;
    }

    @Override
    public User update(UserDTO userDto) {
	User user = findById(userDto.getUserId());
	if (user != null) {
	    BeanUtils.copyProperties(userDto, user, "password");
	    if (user.getUsername().equals("superuser")) {
		user.setRole("ROLE_ADMIN");
	    }
	    userDao.save(user);
	}
	user.setPassword(null);
	return user;
    }

    @Override
    public User save(UserDTO user) {
	User old = userDao.findByUsername(user.getUsername());
	if (old != null) {
	    return null;
	}
	User newUser = new User();
	newUser.setUsername(user.getUsername());
	newUser.setFirstName(user.getFirstName());
	newUser.setLastName(user.getLastName());
	newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
	newUser.setEmail(user.getEmail());
	newUser.setPhone(user.getPhone());
	newUser.setRole("ROLE_USER");
	newUser.setStatus(false);
	newUser = userDao.save(newUser);

	newUser.setPassword(null);
	return newUser;

    }

    @Override
    public List<User> getDistributors() {
	List<User> list = new ArrayList<>();
	userDao.findByRoleAndStatus("ROLE_DISTRIBUTOR", true).iterator().forEachRemaining(list::add);
	return list;
    }

    @Override
    public List<User> findOnlyUsers() {
	List<User> list = new ArrayList<>();
	userDao.findByRoleAndStatus("ROLE_USER", true).iterator().forEachRemaining(list::add);
	return list;
    }

    @Override
    public User toggleStatus(int id) {
	Optional<User> optionalUser = userDao.findById(id);
	if (optionalUser.isPresent()) {
	    User user = optionalUser.get();
	    user.setStatus(!user.isStatus());
	    return userDao.save(user);
	}
	return null;
    }

    @Override
    public Boolean changePassword(UserDTO userDto, String username) {
	User user = userDao.findByUsername(username);
	if (user != null) {
	    if (bcryptEncoder.matches(userDto.getOldPassword(), user.getPassword())) {
		user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
		user.setResetRequested(false);
		userDao.save(user);
		return true;
	    }
	}
	return false;
    }

    @Override
    public Boolean resetPassword(int id) {
	Optional<User> optionalUser = userDao.findById(id);
	if (optionalUser.isPresent()) {
	    User user = optionalUser.get();
	    user.setResetRequested(true);
	    user.setPassword(bcryptEncoder.encode(defaultPassword));
	    userDao.save(user);
	    return true;
	}
	return false;
    }

}
