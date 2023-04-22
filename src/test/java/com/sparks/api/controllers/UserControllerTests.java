package com.sparks.api.controllers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.sparks.api.entities.User;
import com.sparks.api.services.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private Gson gson;

	public UserControllerTests() {
		this.gson = new Gson();
	}

	@BeforeEach
	public void setup() throws InterruptedException, ExecutionException, TimeoutException {
		User userToBeCreated = new User();

		userToBeCreated.setName("Fulano Sicrano");
		userToBeCreated.setEmail("email@example.com");
		userToBeCreated.setPassword("P@ssw0rd");
		userToBeCreated.setCpf("123.456.789-10");
		userToBeCreated.setPhone("+11 (11) 1111-1111");

		User userCreated = new User();

		userCreated.setId("64441357327a68740d94ac26");
		userCreated.setName("Fulano Sicrano");
		userCreated.setEmail("email@example.com");
		userCreated.setPassword("P@ssw0rd");
		userCreated.setCpf("123.456.789-10");
		userCreated.setPhone("+11 (11) 1111-1111");

		User userToUpdate = new User();

		userToUpdate.setPhone("+11 (11) 1111-1112");

		User userUpdated = new User();

		userUpdated.setId("64441357327a68740d94ac26");
		userUpdated.setName("Fulano Sicrano");
		userUpdated.setEmail("email@example.com");
		userUpdated.setPassword("P@ssw0rd");
		userUpdated.setCpf("123.456.789-10");
		userUpdated.setPhone("+11 (11) 1111-1112");

		List<User> users = Arrays.asList(userCreated);

		Mockito.when(userService.createUser(userToBeCreated)).thenReturn(userCreated);
		Mockito.when(userService.findAllUsers()).thenReturn(users);
		Mockito.when(userService.findUserById(userCreated.getId())).thenReturn(userCreated);
		Mockito.when(userService.updateUserById(userCreated.getId(), userToUpdate)).thenReturn(userUpdated);
		Mockito.when(userService.deleteUserById(userCreated.getId())).thenReturn(userUpdated);
	}

	@Test
	public void shouldCreateUser() throws Exception {
		User userToBeCreated = new User();

		userToBeCreated.setName("Fulano Sicrano");
		userToBeCreated.setEmail("email@example.com");
		userToBeCreated.setPassword("P@ssw0rd");
		userToBeCreated.setCpf("123.456.789-10");
		userToBeCreated.setPhone("+11 (11) 1111-1111");

		this.mockMvc
				.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(userToBeCreated)))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldFindAllUsers() throws Exception {
		this.mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	public void shouldFindUserById() throws Exception {
		this.mockMvc.perform(get("/users/{id}", "64441357327a68740d94ac26").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}
	
	@Test
	public void shouldFailToFindUserByIdWhenUserDoesNotExist( ) throws Exception {
		this.mockMvc.perform(get("/users/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldUpdateUserById() throws Exception {
		User userToBeUpdated = new User();

		userToBeUpdated.setPhone("+11 (11) 1111-1112");

		this.mockMvc
				.perform(put("/users/{id}", "64441357327a68740d94ac26").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(userToBeUpdated)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.phone", is("+11 (11) 1111-1112")));
	}
	
	@Test
	public void shouldDeleteUserById() throws Exception {
		this.mockMvc.perform(delete("/users/{id}", "64441357327a68740d94ac26").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}
}
