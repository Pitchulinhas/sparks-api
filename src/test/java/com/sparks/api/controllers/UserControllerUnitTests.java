package com.sparks.api.controllers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.sparks.api.entities.User;
import com.sparks.api.exceptions.BadRequestException;
import com.sparks.api.exceptions.NotFoundException;
import com.sparks.api.services.UserService;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerUnitTests implements IUserControllerUnitTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private Gson gson;

	public UserControllerUnitTests() {
		this.gson = new Gson();
	}

	@BeforeEach
	public void setup() throws Exception {
		// Create

		User createUserInput = new User();

		createUserInput.setName("Fulano Sicrano");
		createUserInput.setEmail("email2023@example.com");
		createUserInput.setPassword("P@ssw0rd");
		createUserInput.setCpf("123.456.789-10");
		createUserInput.setPhone("+11 (11) 1111-1111");

		User userCreated = new User(createUserInput);

		userCreated.setId("64441357327a68740d94ac26");

		User createUserWithExistingEmailInput = new User();

		createUserWithExistingEmailInput.setName("Fulana Sicrana");
		createUserWithExistingEmailInput.setEmail("email2023@example.com");
		createUserWithExistingEmailInput.setPassword("P@ssw0rd");
		createUserWithExistingEmailInput.setCpf("123.456.789-11");
		createUserWithExistingEmailInput.setPhone("+11 (11) 1111-1113");

		// Find all

		List<User> usersFound = Arrays.asList(userCreated);

		// Find by id

		User userFoundById = new User(userCreated);

		// Update by id

		User updateUserByIdInput = new User();

		updateUserByIdInput.setEmail("email23@example.com");

		User userUpdatedById = new User(userCreated);

		userUpdatedById.setEmail("email23@example.com");

		User updateUserWithExistingEmailByIdInput = new User();

		updateUserWithExistingEmailByIdInput.setEmail("email2023@example.com");

		// Delete by id

		User userDeletedById = new User(userUpdatedById);

		// Mocks

		Mockito.when(userService.createUser(createUserInput)).thenReturn(userCreated);
		Mockito.when(userService.createUser(createUserWithExistingEmailInput))
				.thenThrow(new BadRequestException("Já existe um usuário com esse e-mail"));

		Mockito.when(userService.findAllUsers()).thenReturn(usersFound);

		Mockito.when(userService.findUserById("64441357327a68740d94ac26")).thenReturn(userFoundById);
		Mockito.when(userService.findUserById("64441357327a68740d94ac27"))
				.thenThrow(new NotFoundException("Usuário não encontrado"));

		Mockito.when(userService.updateUserById("64441357327a68740d94ac26", updateUserByIdInput)).thenReturn(userUpdatedById);
		Mockito.when(userService.updateUserById("64441357327a68740d94ac27", updateUserByIdInput))
				.thenThrow(new NotFoundException("Usuário não encontrado"));
		Mockito.when(userService.updateUserById("64441357327a68740d94ac26", updateUserWithExistingEmailByIdInput))
				.thenThrow(new BadRequestException("Já existe um usuário com esse e-mail"));

		Mockito.when(userService.deleteUserById("64441357327a68740d94ac26")).thenReturn(userDeletedById);
		Mockito.when(userService.deleteUserById("64441357327a68740d94ac27"))
				.thenThrow(new NotFoundException("Usuário não encontrado"));
	}

	@Test
	public void shouldCreateUser() throws Exception {
		User createUserInput = new User();

		createUserInput.setName("Fulano Sicrano");
		createUserInput.setEmail("email2023@example.com");
		createUserInput.setPassword("P@ssw0rd");
		createUserInput.setCpf("123.456.789-10");
		createUserInput.setPhone("+11 (11) 1111-1111");

		this.mockMvc
				.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(createUserInput)))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldReturnBadRequestStatusCodeWhenCreatingUserAndEmailAlreadyExists() throws Exception {
		User createUserInput = new User();

		createUserInput.setName("Fulana Sicrana");
		createUserInput.setEmail("email2023@example.com");
		createUserInput.setPassword("P@ssw0rd");
		createUserInput.setCpf("123.456.789-11");
		createUserInput.setPhone("+11 (11) 1111-1113");

		this.mockMvc
				.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(createUserInput)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Já existe um usuário com esse e-mail")));
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
	public void shouldReturnNotFoundStatusCodeWhenFindingUserByIdAndUserDoesNotExist() throws Exception {
		this.mockMvc.perform(get("/users/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Usuário não encontrado")));
		;
	}

	@Test
	public void shouldUpdateUserById() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setEmail("email23@example.com");

		this.mockMvc
				.perform(put("/users/{id}", "64441357327a68740d94ac26").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateUserInput)))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.email", is("email23@example.com")));
	}

	@Test
	public void shouldReturnNotFoundStatusCodeWhenUpdatingUserByIdAndUserDoesNotExist() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setEmail("email23@example.com");

		this.mockMvc
				.perform(put("/users/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateUserInput)))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Usuário não encontrado")));
	}

	@Test
	public void shouldReturnBadRequestStatusCodeWhenUpdatingUserByIdAndEmailAlreadyExists() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setEmail("email2023@example.com");

		this.mockMvc
				.perform(put("/users/{id}", "64441357327a68740d94ac26").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateUserInput)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Já existe um usuário com esse e-mail")));
	}

	@Test
	public void shouldDeleteUserById() throws Exception {
		this.mockMvc.perform(delete("/users/{id}", "64441357327a68740d94ac26").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldReturnNotFoundStatusCodeWhenDeletingUserByIdAndUserDoesNotExist() throws Exception {
		this.mockMvc.perform(delete("/users/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Usuário não encontrado")));
		;
	}
}
