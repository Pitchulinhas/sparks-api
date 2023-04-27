package com.sparks.api.services;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.sparks.api.entities.User;
import com.sparks.api.exceptions.BadRequestException;
import com.sparks.api.exceptions.NotFoundException;
import com.sparks.api.producers.UserProducer;
import com.sparks.api.responses.ServiceResponse;

@SpringBootTest(classes = UserService.class)
public class UserServiceUnitTests implements IUserServiceTests {
	@Autowired
	private UserService userService;

	@MockBean
	private UserProducer userProducer;

	@BeforeEach
	public void setup() throws Exception {
		User previouslyCreatedUser = new User();

		previouslyCreatedUser.setId("64441357327a68740d94ac26");
		previouslyCreatedUser.setName("Beatriz Ferreira");
		previouslyCreatedUser.setCpf("123.456.789-11");
		previouslyCreatedUser.setPhone("(11) 91111-1111");
		previouslyCreatedUser.setEmail("beatrizf@email.com");
		previouslyCreatedUser.setPassword("Beatriz@2023");

		/////////////////
		// Create user //
		/////////////////

		User createUserInput = new User();

		createUserInput.setName("André Sousa");
		createUserInput.setCpf("123.456.789-12");
		createUserInput.setPhone("(22) 92222-2222");
		createUserInput.setEmail("andres@email.com");
		createUserInput.setPassword("Andre@2023");

		User userCreated = new User(createUserInput);

		userCreated.setId("64441357327a68740d94ac27");

		ServiceResponse<User> createUserResponse = new ServiceResponse<>();

		createUserResponse.setData(userCreated);

		Mockito.when(userProducer.createUser(createUserInput)).thenReturn(createUserResponse);

		///////////////////////////////////
		// Create user with existing CPF //
		///////////////////////////////////

		User createUserWithExistingCpfInput = new User();

		createUserWithExistingCpfInput.setName("André Sousa");
		createUserWithExistingCpfInput.setCpf("123.456.789-12");
		createUserWithExistingCpfInput.setPhone("(22) 92222-2223");
		createUserWithExistingCpfInput.setEmail("andresousa@email.com");
		createUserWithExistingCpfInput.setPassword("Andre@2023");

		ServiceResponse<User> createUserWithExistingCpfResponse = new ServiceResponse<>();

		createUserWithExistingCpfResponse.setErrorMessage("Já existe um usuário com esse CPF");

		Mockito.when(userProducer.createUser(createUserWithExistingCpfInput))
				.thenReturn(createUserWithExistingCpfResponse);

		/////////////////////////////////////
		// Create user with existing Email //
		/////////////////////////////////////

		User createUserWithExistingEmailInput = new User();

		createUserWithExistingEmailInput.setName("André Silva");
		createUserWithExistingEmailInput.setCpf("123.456.789-13");
		createUserWithExistingEmailInput.setPhone("(33) 93333-3333");
		createUserWithExistingEmailInput.setEmail("andres@email.com");
		createUserWithExistingEmailInput.setPassword("Andre@2023");

		ServiceResponse<User> createUserWithExistingEmailResponse = new ServiceResponse<>();

		createUserWithExistingEmailResponse.setErrorMessage("Já existe um usuário com esse e-mail");

		Mockito.when(userProducer.createUser(createUserWithExistingEmailInput))
				.thenReturn(createUserWithExistingEmailResponse);

		////////////////////
		// Find all users //
		////////////////////

		List<User> usersFound = Arrays.asList(previouslyCreatedUser, userCreated);

		ServiceResponse<List<User>> findAllUsersResponse = new ServiceResponse<>();

		findAllUsersResponse.setData(usersFound);

		Mockito.when(userProducer.findAllUsers()).thenReturn(findAllUsersResponse);

		/////////////////////
		// Find user by id //
		/////////////////////

		User userFoundById = new User(userCreated);

		ServiceResponse<User> findUserByIdResponse = new ServiceResponse<>();

		findUserByIdResponse.setData(userFoundById);

		Mockito.when(userProducer.findUserById(userCreated.getId())).thenReturn(findUserByIdResponse);

		/////////////////////////////////////////
		// Find user that does not exist by id //
		/////////////////////////////////////////

		ServiceResponse<User> findUserThatDoesNotExistByIdResponse = new ServiceResponse<>();

		Mockito.when(userProducer.findUserById("64441357327a68740d94ac28"))
				.thenReturn(findUserThatDoesNotExistByIdResponse);

		///////////////////////
		// Update user by id //
		///////////////////////

		User updateUserByIdInput = new User();

		updateUserByIdInput.setPassword("AndreSousa@2023");

		User userUpdatedById = new User(userCreated);

		userUpdatedById.setPassword(updateUserByIdInput.getPassword());

		ServiceResponse<User> updateUserByIdResponse = new ServiceResponse<>();

		updateUserByIdResponse.setData(userUpdatedById);

		Mockito.when(userProducer.updateUserById(userCreated.getId(), updateUserByIdInput))
				.thenReturn(updateUserByIdResponse);

		/////////////////////////////////////////
		// Update user with existing CPF by id //
		/////////////////////////////////////////

		User updateUserWithExistingCpfByIdInput = new User();

		updateUserWithExistingCpfByIdInput.setCpf(previouslyCreatedUser.getCpf());

		ServiceResponse<User> updateUserWithExistingCpfByIdResponse = new ServiceResponse<>();

		updateUserWithExistingCpfByIdResponse.setErrorMessage("Já existe um usuário com esse CPF");

		Mockito.when(userProducer.updateUserById(userCreated.getId(), updateUserWithExistingCpfByIdInput))
				.thenReturn(updateUserWithExistingCpfByIdResponse);

		///////////////////////////////////////////
		// Update user with existing email by id //
		///////////////////////////////////////////

		User updateUserWithExistingEmailByIdInput = new User();

		updateUserWithExistingEmailByIdInput.setEmail(previouslyCreatedUser.getEmail());

		ServiceResponse<User> updateUserWithExistingEmailByIdResponse = new ServiceResponse<>();

		updateUserWithExistingEmailByIdResponse.setErrorMessage("Já existe um usuário com esse e-mail");

		Mockito.when(userProducer.updateUserById(userCreated.getId(), updateUserWithExistingEmailByIdInput))
				.thenReturn(updateUserWithExistingEmailByIdResponse);

		///////////////////////////////////////////
		// Update user that does not exist by id //
		///////////////////////////////////////////

		User updateUserThatDoesNotExistByIdInput = new User();

		updateUserThatDoesNotExistByIdInput.setEmail("beatrizf@email.com");

		ServiceResponse<User> updateUserThatDoesNotExistByIdResponse = new ServiceResponse<>();

		Mockito.when(userProducer.updateUserById("64441357327a68740d94ac28", updateUserThatDoesNotExistByIdInput))
				.thenReturn(updateUserThatDoesNotExistByIdResponse);

		///////////////////////
		// Delete user by id //
		///////////////////////

		User userDeletedById = new User(userUpdatedById);

		ServiceResponse<User> deleteUserByIdResponse = new ServiceResponse<>();

		deleteUserByIdResponse.setData(userDeletedById);

		Mockito.when(userProducer.deleteUserById(userCreated.getId())).thenReturn(deleteUserByIdResponse);

		///////////////////////////////////////////
		// Delete user that does not exist by id //
		///////////////////////////////////////////

		ServiceResponse<User> deleteUserThatDoesNotExistByIdResponse = new ServiceResponse<>();

		Mockito.when(userProducer.deleteUserById("64441357327a68740d94ac28"))
				.thenReturn(deleteUserThatDoesNotExistByIdResponse);
	}

	@Test
	public void shouldCreateUser() throws Exception {
		User createUserInput = new User();

		createUserInput.setName("André Sousa");
		createUserInput.setCpf("123.456.789-12");
		createUserInput.setPhone("(22) 92222-2222");
		createUserInput.setEmail("andres@email.com");
		createUserInput.setPassword("Andre@2023");

		User userCreated = this.userService.createUser(createUserInput);

		Assertions.assertNotNull(userCreated);
	}

	@Test
	public void shouldThrowBadRequestExceptionWhenCreatingUserAndCpfAlreadyExists() throws Exception {
		User createUserInput = new User();

		createUserInput.setName("André Sousa");
		createUserInput.setCpf("123.456.789-12");
		createUserInput.setPhone("(22) 92222-2223");
		createUserInput.setEmail("andresousa@email.com");
		createUserInput.setPassword("Andre@2023");

		Assertions.assertThrows(BadRequestException.class, () -> {
			this.userService.createUser(createUserInput);
		});
	}

	@Test
	public void shouldThrowBadRequestExceptionWhenCreatingUserAndEmailAlreadyExists() throws Exception {
		User createUserInput = new User();

		createUserInput.setName("André Silva");
		createUserInput.setCpf("123.456.789-13");
		createUserInput.setPhone("(33) 93333-3333");
		createUserInput.setEmail("andres@email.com");
		createUserInput.setPassword("Andre@2023");

		Assertions.assertThrows(BadRequestException.class, () -> {
			this.userService.createUser(createUserInput);
		});
	}

	@Test
	public void shouldFindAllUsers() throws Exception {
		List<User> usersFound = this.userService.findAllUsers();

		Assertions.assertEquals(usersFound.size(), 2);
	}

	@Test
	public void shouldFindUserById() throws Exception {
		User userFound = this.userService.findUserById("64441357327a68740d94ac27");

		Assertions.assertNotNull(userFound);
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenFindingUserByIdAndUserDoesNotExist() throws Exception {
		Assertions.assertThrows(NotFoundException.class, () -> {
			this.userService.findUserById("64441357327a68740d94ac28");
		});
	}

	@Test
	public void shouldUpdateUserById() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setPassword("AndreSousa@2023");

		User userUpdated = this.userService.updateUserById("64441357327a68740d94ac27", updateUserInput);

		Assertions.assertNotNull(userUpdated);
		Assertions.assertEquals(updateUserInput.getPassword(), userUpdated.getPassword());
	}

	@Test
	public void shouldThrowBadRequestExceptionWhenUpdatingUserByIdAndCpfAlreadyExists() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setCpf("123.456.789-11");

		Assertions.assertThrows(BadRequestException.class, () -> {
			this.userService.updateUserById("64441357327a68740d94ac27", updateUserInput);
		});
	}

	@Test
	public void shouldThrowBadRequestExceptionWhenUpdatingUserByIdAndEmailAlreadyExists() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setEmail("beatrizf@email.com");

		Assertions.assertThrows(BadRequestException.class, () -> {
			this.userService.updateUserById("64441357327a68740d94ac27", updateUserInput);
		});
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenUpdatingUserByIdAndUserDoesNotExist() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setEmail("beatrizf@email.com");

		Assertions.assertThrows(NotFoundException.class, () -> {
			this.userService.updateUserById("64441357327a68740d94ac28", updateUserInput);
		});
	}

	@Test
	public void shouldDeleteUserById() throws Exception {
		User userDeleted = this.userService.deleteUserById("64441357327a68740d94ac27");

		Assertions.assertNotNull(userDeleted);
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenDeletingUserByIdAndUserDoesNotExist() throws Exception {
		Assertions.assertThrows(NotFoundException.class, () -> {
			this.userService.deleteUserById("64441357327a68740d94ac28");
		});
	}
}
