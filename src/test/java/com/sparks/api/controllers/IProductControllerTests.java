package com.sparks.api.controllers;

public interface IProductControllerTests {
	public void shouldCreateProduct() throws Exception;

	public void shouldReturnBadRequestStatusCodeWhenCreatingProductAndBarCodeAlreadyExists() throws Exception;

	public void shouldFindAllProducts() throws Exception;

	public void shouldFindProductById() throws Exception;

	public void shouldReturnNotFoundStatusCodeWhenFindingProductByIdAndProductDoesNotExist() throws Exception;

	public void shouldUpdateProductById() throws Exception;

	public void shouldReturnBadRequestStatusCodeWhenUpdatingProductByIdAndBarCodeAlreadyExists() throws Exception;

	public void shouldReturnNotFoundStatusCodeWhenUpdatingProductByIdAndProductDoesNotExist() throws Exception;

	public void shouldDeleteProductById() throws Exception;

	public void shouldReturnNotFoundStatusCodeWhenDeletingProductByIdAndProductDoesNotExist() throws Exception;
}
