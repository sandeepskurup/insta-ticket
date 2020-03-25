package com.instaticket.controllers;

import com.instaticket.bean.ApiResponse;
import com.instaticket.bean.AuthToken;
import com.instaticket.bean.LoginUser;
import com.instaticket.bean.UserDTO;
import com.instaticket.config.JwtTokenUtil;
import com.instaticket.repository.User;
import com.instaticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResponse<AuthToken> register(@RequestBody LoginUser loginUser) throws AuthenticationException {
	authenticationManager.authenticate(
		new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
	final User user = userService.findOne(loginUser.getUsername());
	if (user != null && user.isStatus()) {
	    final String token = jwtTokenUtil.generateToken(user);
	    return new ApiResponse<>(200, "success", new AuthToken(token, user.getUsername(), user.getRole()));
	} else if (user != null && !user.isStatus()) {
	    return new ApiResponse<>(200, "Your account is locked. Please contact us.", null);
	}
	return new ApiResponse<>(200, "Incorrect username or password", null);

    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ApiResponse<User> saveUser(@RequestBody UserDTO user) {
	return new ApiResponse<>(200, "success", userService.save(user));
    }

}
