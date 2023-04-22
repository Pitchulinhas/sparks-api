package com.sparks.api.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sparks.api.entities.User;
import com.sparks.api.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping
	public User createUser(@RequestBody() User user) throws InterruptedException, ExecutionException, TimeoutException {
		try {
			return this.userService.createUser(user);
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro interno");
		}
	}

	@GetMapping
	public List<User> findAllUsers() {
		try {
			return this.userService.findAllUsers();
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro interno");
		}
	}

	@GetMapping("/{id}")
	public User findUserById(@PathVariable("id") String id) {
		try {
			User userFound = this.userService.findUserById(id);

			if (userFound == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
			}

			return userFound;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro interno");
		}
	}

	@PutMapping("/{id}")
	public User updateUserById(@PathVariable("id") String id, @RequestBody User user) {
		try {
			User userUpdated = this.userService.updateUserById(id, user);

			if (userUpdated == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
			}

			return userUpdated;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro interno");
		}
	}

	@DeleteMapping("/{id}")
	public User deleteUserById(@PathVariable("id") String id) {
		try {
			User userDeleted = this.userService.deleteUserById(id);

			if (userDeleted == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
			}

			return userDeleted;
		} catch (ResponseStatusException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro interno");
		}
	}
}
