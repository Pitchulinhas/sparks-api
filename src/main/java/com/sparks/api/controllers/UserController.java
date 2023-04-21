package com.sparks.api.controllers;

import java.util.List;

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
	public User create(@RequestBody() User user) {
		try {
			return this.userService.create(user);
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro interno");
		}
	}

	@GetMapping
	public List<User> findAll() {
		try {
			return this.userService.findAll();
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro interno");
		}
	}

	@GetMapping("/{id}")
	public User findById(@PathVariable("id") String id) {
		try {
			return this.userService.findById(id);
		} catch (Exception ex) {
			if (ex.toString().contains("Payload must not be null")) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
			}

			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro interno");
		}
	}
	
	@PutMapping("/{id}")
	public User updatById(@PathVariable("id") String id, @RequestBody User user) {
		try {
			return this.userService.updateById(id, user);
		} catch (Exception ex) {
			if (ex.toString().contains("Payload must not be null")) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
			}

			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro interno");
		}
	}
	
	@DeleteMapping("/{id}")
	public User deleteById(@PathVariable("id") String id) {
		try {
			return this.userService.deleteById(id);
		} catch (Exception ex) {
			if (ex.toString().contains("Payload must not be null")) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
			}

			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Aconteceu um erro interno");
		}
	}
}
