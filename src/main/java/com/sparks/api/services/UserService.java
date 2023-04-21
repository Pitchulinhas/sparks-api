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
	
	public User create(User user) throws InterruptedException, ExecutionException, TimeoutException {
		return this.userProducer.create(user);
	}
	
	public List<User> findAll() throws InterruptedException, ExecutionException, TimeoutException {
		return this.userProducer.findAll();
	}
	
	public User findById(String id) throws InterruptedException, ExecutionException, TimeoutException {
		return this.userProducer.findById(id);
	}
	
	public User updateById(String id, User user) throws InterruptedException, ExecutionException, TimeoutException {
		return this.userProducer.updateById(id, user);
	}
	
	public User deleteById(String id) throws InterruptedException, ExecutionException, TimeoutException {
		return this.userProducer.deleteById(id);
	}
}
