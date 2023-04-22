package com.sparks.api.services;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.sparks.api.entities.User;
import com.sparks.api.producers.UserProducer;

@SpringBootTest
public class UserServiceTests {
	@Autowired
	private UserService userService;

	@MockBean
	private UserProducer userProducer;

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

		Mockito.when(userProducer.createUser(userToBeCreated)).thenReturn(userCreated);
		Mockito.when(userProducer.findAllUsers()).thenReturn(users);
		Mockito.when(userProducer.findUserById(userCreated.getId())).thenReturn(userCreated);
		Mockito.when(userProducer.updateUserById(userCreated.getId(), userToUpdate)).thenReturn(userUpdated);
		Mockito.when(userProducer.deleteUserById(userCreated.getId())).thenReturn(userUpdated);
	}

	@Test
	public void createUserTest() throws InterruptedException, ExecutionException, TimeoutException {
		User userToBeCreated = new User();

		userToBeCreated.setName("Fulano Sicrano");
		userToBeCreated.setEmail("email@example.com");
		userToBeCreated.setPassword("P@ssw0rd");
		userToBeCreated.setCpf("123.456.789-10");
		userToBeCreated.setPhone("+11 (11) 1111-1111");

		User userCreated = this.userService.createUser(userToBeCreated);

		Assertions.assertNotNull(userCreated);
	}

	@Test
	public void findAllUsersTest() throws InterruptedException, ExecutionException, TimeoutException {
		List<User> usersFound = this.userService.findAllUsers();

		Assertions.assertEquals(usersFound.size(), 1);
	}

	@Test
	public void findUserByIdTest() throws InterruptedException, ExecutionException, TimeoutException {
		User userFound = this.userService.findUserById("64441357327a68740d94ac26");

		Assertions.assertNotNull(userFound);
	}

	@Test
	public void updateUserByIdTest() throws InterruptedException, ExecutionException, TimeoutException {
		User userToBeUpdated = new User();

		userToBeUpdated.setPhone("+11 (11) 1111-1112");

		User userUpdated = this.userService.updateUserById("64441357327a68740d94ac26", userToBeUpdated);

		Assertions.assertEquals(userToBeUpdated.getPhone(), userUpdated.getPhone());
	}

	@Test
	public void deleteUserByIdTest() throws InterruptedException, ExecutionException, TimeoutException {
		User userDeleted = this.userService.deleteUserById("64441357327a68740d94ac26");

		Assertions.assertNotNull(userDeleted);
	}
}
