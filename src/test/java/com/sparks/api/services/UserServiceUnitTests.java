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
import org.springframework.test.context.ActiveProfiles;

import com.sparks.api.entities.User;
import com.sparks.api.exceptions.BadRequestException;
import com.sparks.api.exceptions.NotFoundException;
import com.sparks.api.producers.UserProducer;
import com.sparks.api.responses.ServiceResponse;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceUnitTests implements IUserServiceUnitTests {
	@Autowired
	private UserService userService;

	@MockBean
	private UserProducer userProducer;

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
		
		ServiceResponse<User> createUserResponse = new ServiceResponse<>();
		
		createUserResponse.setData(userCreated);

		User createUserWithExistingEmailInput = new User();

		createUserWithExistingEmailInput.setName("Fulana Sicrana");
		createUserWithExistingEmailInput.setEmail("email2023@example.com");
		createUserWithExistingEmailInput.setPassword("P@ssw0rd");
		createUserWithExistingEmailInput.setCpf("123.456.789-11");
		createUserWithExistingEmailInput.setPhone("+11 (11) 1111-1113");

		ServiceResponse<User> createUserWithExistingEmailResponse = new ServiceResponse<>();

		createUserWithExistingEmailResponse.setErrorMessage("Já existe um usuário com esse e-mail");

		// Find all

		List<User> usersFound = Arrays.asList(userCreated);

		ServiceResponse<List<User>> findAllUsersResponse = new ServiceResponse<>();

		findAllUsersResponse.setData(usersFound);

		// Find by id

		User userFoundById = new User(userCreated);

		ServiceResponse<User> findUserByIdResponse = new ServiceResponse<>();

		findUserByIdResponse.setData(userFoundById);

		ServiceResponse<User> findUserThatDoesNotExistByIdResponse = new ServiceResponse<>();

		// Update by id

		User updateUserByIdInput = new User();

		updateUserByIdInput.setEmail("email23@example.com");

		User userUpdatedById = new User(userCreated);
		
		userUpdatedById.setEmail("email23@example.com");
		
		ServiceResponse<User> updateUserByIdResponse = new ServiceResponse<>();
		
		updateUserByIdResponse.setData(userUpdatedById);

		User updateUserWithExistingEmailByIdInput = new User();

		updateUserWithExistingEmailByIdInput.setEmail("email2023@example.com");

		ServiceResponse<User> updateUserWithExistingEmailByIdResponse = new ServiceResponse<>();

		updateUserWithExistingEmailByIdResponse.setErrorMessage("Já existe um usuário com esse e-mail");

		ServiceResponse<User> updateUserThatDoesNotExistByIdResponse = new ServiceResponse<>();

		// Delete by id

		User userDeletedById = new User(userUpdatedById);

		ServiceResponse<User> deleteUserByIdResponse = new ServiceResponse<>();

		deleteUserByIdResponse.setData(userDeletedById);

		ServiceResponse<User> deleteUserThatDoesNotExistByIdResponse = new ServiceResponse<>();

		// Mocks

		Mockito.when(userProducer.createUser(createUserInput)).thenReturn(createUserResponse);
		Mockito.when(userProducer.createUser(createUserWithExistingEmailInput))
				.thenReturn(createUserWithExistingEmailResponse);

		Mockito.when(userProducer.findAllUsers()).thenReturn(findAllUsersResponse);

		Mockito.when(userProducer.findUserById("64441357327a68740d94ac26")).thenReturn(findUserByIdResponse);
		Mockito.when(userProducer.findUserById("64441357327a68740d94ac27"))
				.thenReturn(findUserThatDoesNotExistByIdResponse);

		Mockito.when(userProducer.updateUserById("64441357327a68740d94ac26", updateUserByIdInput))
				.thenReturn(updateUserByIdResponse);
		Mockito.when(userProducer.updateUserById("64441357327a68740d94ac26", updateUserWithExistingEmailByIdInput))
				.thenReturn(updateUserWithExistingEmailByIdResponse);
		Mockito.when(userProducer.updateUserById("64441357327a68740d94ac27", updateUserByIdInput))
				.thenReturn(updateUserThatDoesNotExistByIdResponse);

		Mockito.when(userProducer.deleteUserById("64441357327a68740d94ac26")).thenReturn(deleteUserByIdResponse);
		Mockito.when(userProducer.deleteUserById("64441357327a68740d94ac27"))
				.thenReturn(deleteUserThatDoesNotExistByIdResponse);
	}

	@Test
	public void shouldCreateUser() throws Exception {
		User createUserInput = new User();

		createUserInput.setName("Fulano Sicrano");
		createUserInput.setEmail("email2023@example.com");
		createUserInput.setPassword("P@ssw0rd");
		createUserInput.setCpf("123.456.789-10");
		createUserInput.setPhone("+11 (11) 1111-1111");

		User userCreated = this.userService.createUser(createUserInput);

		Assertions.assertNotNull(userCreated);
	}

	@Test
	public void shouldThrowBadRequestExceptionWhenCreatingUserAndEmailAlreadyExists() throws Exception {
		User createUserInput = new User();

		createUserInput.setName("Fulana Sicrana");
		createUserInput.setEmail("email2023@example.com");
		createUserInput.setPassword("P@ssw0rd");
		createUserInput.setCpf("123.456.789-11");
		createUserInput.setPhone("+11 (11) 1111-1113");

		Assertions.assertThrows(BadRequestException.class, () -> {
			this.userService.createUser(createUserInput);
		});
	}

	@Test
	public void shouldFindAllUsers() throws Exception {
		List<User> usersFound = this.userService.findAllUsers();

		Assertions.assertEquals(usersFound.size(), 1);
	}

	@Test
	public void shouldFindUserById() throws Exception {
		User userFound = this.userService.findUserById("64441357327a68740d94ac26");

		Assertions.assertNotNull(userFound);
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenFindingUserByIdAndUserDoesNotExist() throws Exception {
		Assertions.assertThrows(NotFoundException.class, () -> {
			this.userService.findUserById("64441357327a68740d94ac27");
		});
	}

	@Test
	public void shouldUpdateUserById() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setEmail("email23@example.com");

		User userUpdated = this.userService.updateUserById("64441357327a68740d94ac26", updateUserInput);

		Assertions.assertNotNull(userUpdated);

		Assertions.assertEquals(updateUserInput.getEmail(), userUpdated.getEmail());
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenUpdatingUserByIdAndUserDoesNotExist() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setEmail("email23@example.com");

		Assertions.assertThrows(NotFoundException.class, () -> {
			this.userService.updateUserById("64441357327a68740d94ac27", updateUserInput);
		});
	}

	@Test
	public void shouldThrowBadRequestExceptionWhenUpdatingUserByIdAndEmailAlreadyExists() throws Exception {
		User updateUserInput = new User();

		updateUserInput.setEmail("email2023@example.com");

		Assertions.assertThrows(BadRequestException.class, () -> {
			this.userService.updateUserById("64441357327a68740d94ac26", updateUserInput);
		});
	}

	@Test
	public void shouldDeleteUserById() throws Exception {
		User userDeleted = this.userService.deleteUserById("64441357327a68740d94ac26");

		Assertions.assertNotNull(userDeleted);
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenDeletingUserByIdAndUserDoesNotExist() throws Exception {
		Assertions.assertThrows(NotFoundException.class, () -> {
			this.userService.deleteUserById("64441357327a68740d94ac27");
		});
	}
}
