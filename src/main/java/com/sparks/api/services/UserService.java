package com.sparks.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sparks.api.entities.User;
import com.sparks.api.exceptions.BadRequestException;
import com.sparks.api.exceptions.NotFoundException;
import com.sparks.api.producers.UserProducer;
import com.sparks.api.responses.ServiceResponse;

@Service
public class UserService {
	@Autowired
	private UserProducer userProducer;

	public User createUser(User createUserInput) throws Exception {
		ServiceResponse<User> response = this.userProducer.createUser(createUserInput);
		
		if (response.getErrorMessage() != null) {
			throw new BadRequestException(response.getErrorMessage());
		}
		
		return response.getData();
	}

	public List<User> findAllUsers() throws Exception {
		ServiceResponse<List<User>> response = this.userProducer.findAllUsers();
		
		if (response.getErrorMessage() != null) {
			throw new BadRequestException(response.getErrorMessage());
		}
		
		return response.getData();
	}

	public User findUserById(String id) throws Exception {
		ServiceResponse<User> response = this.userProducer.findUserById(id);
		
		if (response.getErrorMessage() != null) {
			throw new BadRequestException(response.getErrorMessage());
		}
		
		if (response.getData() == null) {
			throw new NotFoundException("Usuário não encontrado");
		}
		
		return response.getData();
	}

	public User updateUserById(String id, User updateUserInput) throws Exception {
		ServiceResponse<User> response = this.userProducer.updateUserById(id, updateUserInput);
		
		if (response.getErrorMessage() != null) {
			throw new BadRequestException(response.getErrorMessage());
		}
		
		if (response.getData() == null) {
			throw new NotFoundException("Usuário não encontrado");
		}
		
		return response.getData();
	}

	public User deleteUserById(String id) throws Exception {
		ServiceResponse<User> response = this.userProducer.deleteUserById(id);
		
		if (response.getErrorMessage() != null) {
			throw new BadRequestException(response.getErrorMessage());
		}
		
		if (response.getData() == null) {
			throw new NotFoundException("Usuário não encontrado");
		}
		
		return response.getData();
	}
}
