package com.sparks.api.services;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sparks.api.entities.User;
import com.sparks.api.producers.UserProducer;

@Service
public class UserService {
	@Autowired
	private UserProducer userProducer;

	public User createUser(User user) throws InterruptedException, ExecutionException, TimeoutException {
		return this.userProducer.createUser(user);
	}

	public List<User> findAllUsers() throws InterruptedException, ExecutionException, TimeoutException {
		return this.userProducer.findAllUsers();
	}

	public User findUserById(String id) throws InterruptedException, ExecutionException, TimeoutException {
		return this.userProducer.findUserById(id);
	}

	public User updateUserById(String id, User user) throws InterruptedException, ExecutionException, TimeoutException {
		return this.userProducer.updateUserById(id, user);
	}

	public User deleteUserById(String id) throws InterruptedException, ExecutionException, TimeoutException {
		return this.userProducer.deleteUserById(id);
	}
}
