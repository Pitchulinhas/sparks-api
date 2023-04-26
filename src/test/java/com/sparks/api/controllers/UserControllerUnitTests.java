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
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.sparks.api.entities.User;
import com.sparks.api.exceptions.BadRequestException;
import com.sparks.api.exceptions.NotFoundException;
import com.sparks.api.services.UserService;

@WebMvcTest(UserController.class)
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
		User previouslyCreatedUser = new User();

		previouslyCreatedUser.setId("64441357327a68740d94ac26");
		previouslyCreatedUser.setName("André Silva");
		previouslyCreatedUser.setCpf("123.456.789-11");
		previouslyCreatedUser.setPhone("(11) 91111-1111");
		previouslyCreatedUser.setEmail("andres@email.com");
		previouslyCreatedUser.setPassword("Andre@2023");

		/////////////////
		// Create user //
		/////////////////

		User createUserInput = new User();

		createUserInput.setName("André Sousa");
		createUserInput.setCpf("123.456.789-12");
		createUserInput.setPhone("(22) 92222-2222");
		createUserInput.setEmail("andresousa@email.com");
		createUserInput.setPassword("Andre@2023");

		User userCreated = new User(createUserInput);

		userCreated.setId("64441357327a68740d94ac27");

		Mockito.when(userService.createUser(createUserInput)).thenReturn(userCreated);

		///////////////////////////////////
		// Create user with existing CPF //
		///////////////////////////////////

		User createUserWithExistingCpfInput = new User();

		createUserWithExistingCpfInput.setName("André Silva");
		createUserWithExistingCpfInput.setCpf("123.456.789-11");
		createUserWithExistingCpfInput.setPhone("(11) 91111-1112");
		createUserWithExistingCpfInput.setEmail("andres@email.com");
		createUserWithExistingCpfInput.setPassword("Andre@2023");

		Mockito.when(userService.createUser(createUserWithExistingCpfInput))
				.thenThrow(new BadRequestException("Já existe um usuário com esse CPF"));

		/////////////////////////////////////
		// Create user with existing Email //
		/////////////////////////////////////

		User createUserWithExistingEmailInput = new User();

		createUserWithExistingEmailInput.setName("André Silva");
		createUserWithExistingEmailInput.setCpf("123.456.789-13");
		createUserWithExistingEmailInput.setPhone("(11) 91111-1112");
		createUserWithExistingEmailInput.setEmail("andres@email.com");
		createUserWithExistingEmailInput.setPassword("Andre@2023");

		Mockito.when(userService.createUser(createUserWithExistingEmailInput))
				.thenThrow(new BadRequestException("Já existe um usuário com esse e-mail"));

		////////////////////
		// Find all users //
		////////////////////

		List<User> usersFound = Arrays.asList(previouslyCreatedUser, userCreated);

		Mockito.when(userService.findAllUsers()).thenReturn(usersFound);

		/////////////////////
		// Find user by id //
		/////////////////////

		User userFoundById = new User(userCreated);

		Mockito.when(userService.findUserById(userCreated.getId())).thenReturn(userFoundById);

		/////////////////////////////////////////
		// Find user that does not exist by id //
		/////////////////////////////////////////

		Mockito.when(userService.findUserById("64441357327a68740d94ac28"))
				.thenThrow(new NotFoundException("Usuário não encontrado"));

		///////////////////////
		// Update user by id //
		///////////////////////

		User updateUserByIdInput = new User();

		updateUserByIdInput.setPassword("AndreSousa@2023");

		User userUpdatedById = new User(userCreated);

		userUpdatedById.setPassword(updateUserByIdInput.getPassword());

		Mockito.when(userService.updateUserById(userCreated.getId(), updateUserByIdInput)).thenReturn(userUpdatedById);

		/////////////////////////////////////////
		// Update user with existing CPF by id //
		/////////////////////////////////////////

		User updateUserWithExistingCpfByIdInput = new User();

		updateUserWithExistingCpfByIdInput.setCpf(previouslyCreatedUser.getCpf());

		Mockito.when(userService.updateUserById(userCreated.getId(), updateUserWithExistingCpfByIdInput))
				.thenThrow(new BadRequestException("Já existe um usuário com esse CPF"));

		///////////////////////////////////////////
		// Update user with existing email by id //
		///////////////////////////////////////////

		User updateUserWithExistingEmailByIdInput = new User();

		updateUserWithExistingEmailByIdInput.setEmail(previouslyCreatedUser.getEmail());

		Mockito.when(userService.updateUserById(userCreated.getId(), updateUserWithExistingEmailByIdInput))
				.thenThrow(new BadRequestException("Já existe um usuário com esse e-mail"));

		///////////////////////////////////////////
		// Update user that does not exist by id //
		///////////////////////////////////////////

		User updateUserThatDoesNotExistByIdInput = new User();

		updateUserThatDoesNotExistByIdInput.setEmail("beatrizf@email.com");

		Mockito.when(userService.updateUserById("64441357327a68740d94ac28", updateUserThatDoesNotExistByIdInput))
				.thenThrow(new NotFoundException("Usuário não encontrado"));

		///////////////////////
		// Delete user by id //
		///////////////////////

		User userDeletedById = new User(userUpdatedById);

		Mockito.when(userService.deleteUserById(userCreated.getId())).thenReturn(userDeletedById);

		///////////////////////////////////////////
		// Delete user that does not exist by id //
		///////////////////////////////////////////

		Mockito.when(userService.deleteUserById("64441357327a68740d94ac28"))
				.thenThrow(new NotFoundException("Usuário não encontrado"));
	}

	@Test
	public void shouldCreateUser() throws Exception {
		User createUserInput = new User();

		createUserInput.setName("André Sousa");
		createUserInput.setCpf("123.456.789-12");
		createUserInput.setPhone("(22) 92222-2222");
		createUserInput.setEmail("andresousa@email.com");
		createUserInput.setPassword("Andre@2023");

		this.mockMvc
				.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(createUserInput)))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldReturnBadRequestStatusCodeWhenCreatingUserAndCpfAlreadyExists() throws Exception {
		User createUserInput = new User();

		createUserInput.setName("André Silva");
		createUserInput.setCpf("123.456.789-11");
		createUserInput.setPhone("(11) 91111-1112");
		createUserInput.setEmail("andres@email.com");
		createUserInput.setPassword("Andre@2023");

		this.mockMvc
				.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(createUserInput)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Já existe um usuário com esse CPF")));
	}

	@Test
	public void shouldReturnBadRequestStatusCodeWhenCreatingUserAndEmailAlreadyExists() throws Exception {
		User createUserInput = new User();

		createUserInput.setName("André Silva");
		createUserInput.setCpf("123.456.789-13");
		createUserInput.setPhone("(11) 91111-1112");
		createUserInput.setEmail("andres@email.com");
		createUserInput.setPassword("Andre@2023");

		this.mockMvc
				.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(gson.toJson(createUserInput)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Já existe um usuário com esse e-mail")));
	}

	@Test
	public void shouldFindAllUsers() throws Exception {
		this.mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void shouldFindUserById() throws Exception {
		this.mockMvc.perform(get("/users/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldReturnNotFoundStatusCodeWhenFindingUserByIdAndUserDoesNotExist() throws Exception {
		this.mockMvc.perform(get("/users/{id}", "64441357327a68740d94ac28").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Usuário não encontrado")));
		;
	}

	@Test
	public void shouldUpdateUserById() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setPassword("AndreSousa@2023");

		this.mockMvc
				.perform(put("/users/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateUserInput)))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.password", is(updateUserInput.getPassword())));
	}

	@Test
	public void shouldReturnBadRequestStatusCodeWhenUpdatingUserByIdAndCpfAlreadyExists() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setCpf("123.456.789-11");

		this.mockMvc
				.perform(put("/users/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateUserInput)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Já existe um usuário com esse CPF")));
	}

	@Test
	public void shouldReturnBadRequestStatusCodeWhenUpdatingUserByIdAndEmailAlreadyExists() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setEmail("andres@email.com");

		this.mockMvc
				.perform(put("/users/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateUserInput)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Já existe um usuário com esse e-mail")));
	}

	@Test
	public void shouldReturnNotFoundStatusCodeWhenUpdatingUserByIdAndUserDoesNotExist() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setEmail("beatrizf@email.com");

		this.mockMvc
				.perform(put("/users/{id}", "64441357327a68740d94ac28").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateUserInput)))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Usuário não encontrado")));
	}

	@Test
	public void shouldDeleteUserById() throws Exception {
		this.mockMvc.perform(delete("/users/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldReturnNotFoundStatusCodeWhenDeletingUserByIdAndUserDoesNotExist() throws Exception {
		this.mockMvc.perform(delete("/users/{id}", "64441357327a68740d94ac28").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Usuário não encontrado")));
		;
	}
}
