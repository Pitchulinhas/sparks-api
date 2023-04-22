package com.sparks.api.entities;

import lombok.Data;

@Data
public class User {
	private String id;

	private String name;
	private String email;
	private String password;
	private String phone;
	private String cpf;
}
