package com.sparks.api.services;

public interface IProductServiceTests {
	public void shouldCreateProduct() throws Exception;

	public void shouldThrowBadRequestExceptionWhenCreatingProductAndBarCodeAlreadyExists() throws Exception;

	public void shouldFindAllProducts() throws Exception;

	public void shouldFindProductById() throws Exception;

	public void shouldThrowNotFoundExceptionWhenFindingProductByIdAndProductDoesNotExist() throws Exception;

	public void shouldUpdateProductById() throws Exception;

	public void shouldThrowNotFoundExceptionWhenUpdatingProductByIdAndProductDoesNotExist() throws Exception;

	public void shouldThrowBadRequestExceptionWhenUpdatingProductByIdAndBarCodeAlreadyExists() throws Exception;

	public void shouldDeleteProductById() throws Exception;

	public void shouldThrowNotFoundExceptionWhenDeletingProductByIdAndProductDoesNotExist() throws Exception;
}
