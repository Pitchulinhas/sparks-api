package com.sparks.api.services;

public interface IUserServiceTests {
	public void shouldCreateUser() throws Exception;

	public void shouldThrowBadRequestExceptionWhenCreatingUserAndEmailAlreadyExists() throws Exception;

	public void shouldFindAllUsers() throws Exception;

	public void shouldFindUserById() throws Exception;

	public void shouldThrowNotFoundExceptionWhenFindingUserByIdAndUserDoesNotExist() throws Exception;

	public void shouldUpdateUserById() throws Exception;

	public void shouldThrowNotFoundExceptionWhenUpdatingUserByIdAndUserDoesNotExist() throws Exception;

	public void shouldThrowBadRequestExceptionWhenUpdatingUserByIdAndCpfAlreadyExists() throws Exception;
	
	public void shouldThrowBadRequestExceptionWhenUpdatingUserByIdAndEmailAlreadyExists() throws Exception;

	public void shouldDeleteUserById() throws Exception;

	public void shouldThrowNotFoundExceptionWhenDeletingUserByIdAndUserDoesNotExist() throws Exception;
}
