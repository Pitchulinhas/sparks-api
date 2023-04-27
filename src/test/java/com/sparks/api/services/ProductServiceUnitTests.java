package com.sparks.api.services;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.sparks.api.entities.Product;
import com.sparks.api.exceptions.BadRequestException;
import com.sparks.api.exceptions.NotFoundException;
import com.sparks.api.producers.ProductProducer;
import com.sparks.api.responses.ServiceResponse;

@SpringBootTest(classes = ProductService.class)
public class ProductServiceUnitTests implements IProductServiceTests {
	@Autowired
	private ProductService productService;

	@MockBean
	private ProductProducer productProducer;

	@BeforeEach
	public void setup() throws Exception {
		Product previouslyCreatedProduct = new Product();

		previouslyCreatedProduct.setId("64441357327a68740d94ac26");
		previouslyCreatedProduct.setName("Arroz Caçarola Parbolizado 1Kg");
		previouslyCreatedProduct.setBarCode("17896393601012");
		previouslyCreatedProduct.setPrice(4.25);
		previouslyCreatedProduct
				.setPicture("https://supermercadojequie.com/image/cache/catalog/produtos/7896393601046-1000x1000.jpg");
		previouslyCreatedProduct.setAvailable(1000);

		////////////////////
		// Create product //
		////////////////////

		Product createProductInput = new Product();

		createProductInput.setName("Arroz Kika Parbolizado 1Kg");
		createProductInput.setBarCode("7897586400026");
		createProductInput.setPicture(
				"https://lojacentraldealimentos.com.br/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/k/i/kika30.jpg");
		createProductInput.setPrice(4.25);
		createProductInput.setAvailable(1000);

		Product productCreated = new Product(createProductInput);

		productCreated.setId("64441357327a68740d94ac27");

		ServiceResponse<Product> createProductResponse = new ServiceResponse<>();

		createProductResponse.setData(productCreated);

		Mockito.when(productProducer.createProduct(createProductInput)).thenReturn(createProductResponse);

		///////////////////////////////////////////
		// Create product with existing bar code //
		///////////////////////////////////////////

		Product createProductWithExistingBarCodeInput = new Product();

		createProductWithExistingBarCodeInput.setName("Arroz Kika Parbolizado 1Kg");
		createProductWithExistingBarCodeInput.setBarCode("7897586400026");
		createProductWithExistingBarCodeInput.setPicture(
				"https://lojacentraldealimentos.com.br/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/k/i/kika30.jpg");
		createProductWithExistingBarCodeInput.setPrice(4.35);
		createProductWithExistingBarCodeInput.setAvailable(1000);

		ServiceResponse<Product> createProductWithExistingBarCodeResponse = new ServiceResponse<>();

		createProductWithExistingBarCodeResponse.setErrorMessage("Já existe um produto com esse código de barras");

		Mockito.when(productProducer.createProduct(createProductWithExistingBarCodeInput))
				.thenReturn(createProductWithExistingBarCodeResponse);

		///////////////////////
		// Find all products //
		///////////////////////

		List<Product> productsFound = Arrays.asList(previouslyCreatedProduct, productCreated);

		ServiceResponse<List<Product>> findAllProductsResponse = new ServiceResponse<>();

		findAllProductsResponse.setData(productsFound);

		Mockito.when(productProducer.findAllProducts()).thenReturn(findAllProductsResponse);

		////////////////////////
		// Find product by id //
		////////////////////////

		Product productFoundById = new Product(productCreated);

		ServiceResponse<Product> findProductByIdResponse = new ServiceResponse<>();

		findProductByIdResponse.setData(productFoundById);

		Mockito.when(productProducer.findProductById(productCreated.getId())).thenReturn(findProductByIdResponse);

		////////////////////////////////////////////
		// Find product that does not exist by id //
		////////////////////////////////////////////

		ServiceResponse<Product> findProductThatDoesNotExistByIdResponse = new ServiceResponse<>();

		Mockito.when(productProducer.findProductById("64441357327a68740d94ac28"))
				.thenReturn(findProductThatDoesNotExistByIdResponse);

		//////////////////////////
		// Update product by id //
		//////////////////////////

		Product updateProductByIdInput = new Product();

		updateProductByIdInput.setAvailable(900);

		Product productUpdatedById = new Product(productCreated);

		productUpdatedById.setAvailable(900);

		ServiceResponse<Product> updateProductByIdResponse = new ServiceResponse<>();

		updateProductByIdResponse.setData(productUpdatedById);

		Mockito.when(productProducer.updateProductById(productCreated.getId(), updateProductByIdInput))
				.thenReturn(updateProductByIdResponse);

		/////////////////////////////////////////////////
		// Update product with existing bar code by id //
		/////////////////////////////////////////////////

		Product updateProductWithExistingBarCodeByIdInput = new Product();

		updateProductWithExistingBarCodeByIdInput.setBarCode(previouslyCreatedProduct.getBarCode());

		ServiceResponse<Product> updateProductWithExistingBarCodeByIdResponse = new ServiceResponse<>();

		updateProductWithExistingBarCodeByIdResponse.setErrorMessage("Já existe um produto com esse código de barras");

		Mockito.when(
				productProducer.updateProductById(productCreated.getId(), updateProductWithExistingBarCodeByIdInput))
				.thenReturn(updateProductWithExistingBarCodeByIdResponse);

		//////////////////////////////////////////////
		// Update product that does not exist by id //
		//////////////////////////////////////////////

		Product updateProductThatDoesNotExistByIdInput = new Product();

		updateProductThatDoesNotExistByIdInput.setAvailable(900);

		ServiceResponse<Product> updateProductThatDoesNotExistByIdResponse = new ServiceResponse<>();

		Mockito.when(
				productProducer.updateProductById("64441357327a68740d94ac28", updateProductThatDoesNotExistByIdInput))
				.thenReturn(updateProductThatDoesNotExistByIdResponse);

		//////////////////////////
		// Delete product by id //
		//////////////////////////

		Product productDeletedById = new Product(productUpdatedById);

		ServiceResponse<Product> deleteProductByIdResponse = new ServiceResponse<>();

		deleteProductByIdResponse.setData(productDeletedById);

		Mockito.when(productProducer.deleteProductById(productCreated.getId())).thenReturn(deleteProductByIdResponse);

		//////////////////////////////////////////////
		// Delete product that does not exist by id //
		//////////////////////////////////////////////

		ServiceResponse<Product> deleteProductThatDoesNotExistByIdResponse = new ServiceResponse<>();

		Mockito.when(productProducer.deleteProductById("64441357327a68740d94ac28"))
				.thenReturn(deleteProductThatDoesNotExistByIdResponse);
	}

	@Test
	public void shouldCreateProduct() throws Exception {
		Product createProductInput = new Product();

		createProductInput.setName("Arroz Kika Parbolizado 1Kg");
		createProductInput.setBarCode("7897586400026");
		createProductInput.setPicture(
				"https://lojacentraldealimentos.com.br/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/k/i/kika30.jpg");
		createProductInput.setPrice(4.25);
		createProductInput.setAvailable(1000);

		Product productCreated = this.productService.createProduct(createProductInput);

		Assertions.assertNotNull(productCreated);
	}

	@Test
	public void shouldThrowBadRequestExceptionWhenCreatingProductAndBarCodeAlreadyExists() throws Exception {
		Product createProductInput = new Product();

		createProductInput.setName("Arroz Kika Parbolizado 1Kg");
		createProductInput.setBarCode("7897586400026");
		createProductInput.setPicture(
				"https://lojacentraldealimentos.com.br/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/k/i/kika30.jpg");
		createProductInput.setPrice(4.35);
		createProductInput.setAvailable(1000);

		Assertions.assertThrows(BadRequestException.class, () -> {
			this.productService.createProduct(createProductInput);
		});
	}

	@Test
	public void shouldFindAllProducts() throws Exception {
		List<Product> productsFound = this.productService.findAllProducts();

		Assertions.assertEquals(productsFound.size(), 2);
	}

	@Test
	public void shouldFindProductById() throws Exception {
		Product productFound = this.productService.findProductById("64441357327a68740d94ac27");

		Assertions.assertNotNull(productFound);
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenFindingProductByIdAndProductDoesNotExist() throws Exception {
		Assertions.assertThrows(NotFoundException.class, () -> {
			this.productService.findProductById("64441357327a68740d94ac28");
		});
	}

	@Test
	public void shouldUpdateProductById() throws Exception {
		Product updateProductInput = new Product();

		updateProductInput.setAvailable(900);

		Product productUpdated = this.productService.updateProductById("64441357327a68740d94ac27", updateProductInput);

		Assertions.assertNotNull(productUpdated);
		Assertions.assertEquals(updateProductInput.getAvailable(), productUpdated.getAvailable());
	}

	@Test
	public void shouldThrowBadRequestExceptionWhenUpdatingProductByIdAndBarCodeAlreadyExists() throws Exception {
		Product updateProductInput = new Product();

		updateProductInput.setBarCode("17896393601012");

		Assertions.assertThrows(BadRequestException.class, () -> {
			this.productService.updateProductById("64441357327a68740d94ac27", updateProductInput);
		});
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenUpdatingProductByIdAndProductDoesNotExist() throws Exception {
		Product updateProductInput = new Product();

		updateProductInput.setAvailable(900);

		Assertions.assertThrows(NotFoundException.class, () -> {
			this.productService.updateProductById("64441357327a68740d94ac28", updateProductInput);
		});
	}

	@Test
	public void shouldDeleteProductById() throws Exception {
		Product productDeleted = this.productService.deleteProductById("64441357327a68740d94ac27");

		Assertions.assertNotNull(productDeleted);
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenDeletingProductByIdAndProductDoesNotExist() throws Exception {
		Assertions.assertThrows(NotFoundException.class, () -> {
			this.productService.deleteProductById("64441357327a68740d94ac28");
		});
	}
}
