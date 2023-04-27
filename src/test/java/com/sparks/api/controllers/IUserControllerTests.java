package com.sparks.api.controllers;

public interface IUserControllerTests {
	public void shouldCreateUser() throws Exception;

	public void shouldReturnBadRequestStatusCodeWhenCreatingUserAndEmailAlreadyExists() throws Exception;

	public void shouldFindAllUsers() throws Exception;

	public void shouldFindUserById() throws Exception;

	public void shouldReturnNotFoundStatusCodeWhenFindingUserByIdAndUserDoesNotExist() throws Exception;

	public void shouldUpdateUserById() throws Exception;

	public void shouldReturnBadRequestStatusCodeWhenUpdatingUserByIdAndCpfAlreadyExists() throws Exception;
	
	public void shouldReturnBadRequestStatusCodeWhenUpdatingUserByIdAndEmailAlreadyExists() throws Exception;

	public void shouldReturnNotFoundStatusCodeWhenUpdatingUserByIdAndUserDoesNotExist() throws Exception;

	public void shouldDeleteUserById() throws Exception;

	public void shouldReturnNotFoundStatusCodeWhenDeletingUserByIdAndUserDoesNotExist() throws Exception;
}
