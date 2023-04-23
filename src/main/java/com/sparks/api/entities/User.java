package com.sparks.api.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
	private String id;
	private String name;
	private String email;
	private String password;
	private String phone;
	private String cpf;
	
	public User(User user) {
		this.id = user.id;
		this.name = user.name;
		this.email = user.email;
		this.password = user.password;
		this.phone = user.phone;
		this.cpf = user.cpf;
	}
}
