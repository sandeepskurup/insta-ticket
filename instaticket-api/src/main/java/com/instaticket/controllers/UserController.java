package com.instaticket.controllers;

import java.util.List;

import com.instaticket.bean.ApiResponse;
import com.instaticket.bean.UserDTO;
import com.instaticket.repository.User;
import com.instaticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<User> saveUser(@RequestBody UserDTO user) {
	return new ApiResponse<>(HttpStatus.OK.value(), "User saved successfully.", userService.save(user));
    }

    @GetMapping("/getUserDetails")
    public ApiResponse<List<User>> getUserDetails(Authentication auth) {
	User user = userService.findOne(auth.getName());
	user.setPassword(null);
	return new ApiResponse<>(HttpStatus.OK.value(), "User details fetched successfully", user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<User>> listUser() {
	return new ApiResponse<>(HttpStatus.OK.value(), "User list fetched successfully.", userService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllUsers")
    public ApiResponse<List<User>> getAllUsers() {
	return new ApiResponse<>(HttpStatus.OK.value(), "User list fetched successfully.", userService.findOnlyUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/getAnyUserDetails", method = RequestMethod.POST, consumes = "text/plain")
    public ApiResponse<List<User>> getDetails(@RequestBody String username) {
	User user = userService.findOne(username);
	user.setPassword(null);
	return new ApiResponse<>(HttpStatus.OK.value(), "User details fetched successfully", user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/toggleStatus/{id}")
    public ApiResponse<User> toggleStatus(@PathVariable int id) {
	return new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully.", userService.toggleStatus(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<User> getOne(@PathVariable int id) {
	return new ApiResponse<>(HttpStatus.OK.value(), "User fetched successfully.", userService.findById(id));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<User> update(@RequestBody UserDTO userDto) {
	return new ApiResponse<>(HttpStatus.OK.value(), "User updated successfully.", userService.update(userDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable int id) {
	userService.delete(id);
	return new ApiResponse<>(HttpStatus.OK.value(), "User deleted successfully.", null);
    }

    @GetMapping("/getDistributors")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<User>> getDistributors() {
	return new ApiResponse<>(HttpStatus.OK.value(), "Distributors fetched successfully.",
		userService.getDistributors());
    }

    @PostMapping("/changePassword")
    public ApiResponse<Boolean> changePassword(@RequestBody UserDTO userDto, Authentication auth) {
	return new ApiResponse<>(HttpStatus.OK.value(), "Password Changed successfully.",
		userService.changePassword(userDto, auth.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/resetPassword/{id}")
    public ApiResponse<Boolean> resetPassword(@PathVariable int id) {
	return new ApiResponse<>(HttpStatus.OK.value(), "Password reset successfully.", userService.resetPassword(id));
    }

}
