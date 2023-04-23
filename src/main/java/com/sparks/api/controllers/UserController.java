package com.sparks.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparks.api.entities.User;
import com.sparks.api.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping
	public User createUser(@RequestBody() User createUserInput) throws Exception {
		return this.userService.createUser(createUserInput);
	}

	@GetMapping
	public List<User> findAllUsers() throws Exception {
		return this.userService.findAllUsers();
	}

	@GetMapping("/{id}")
	public User findUserById(@PathVariable("id") String id) throws Exception {
		return this.userService.findUserById(id);
	}

	@PutMapping("/{id}")
	public User updateUserById(@PathVariable("id") String id, @RequestBody User updateUserInput) throws Exception {
		return this.userService.updateUserById(id, updateUserInput);
	}

	@DeleteMapping("/{id}")
	public User deleteUserById(@PathVariable("id") String id) throws Exception {
		return this.userService.deleteUserById(id);
	}
}
