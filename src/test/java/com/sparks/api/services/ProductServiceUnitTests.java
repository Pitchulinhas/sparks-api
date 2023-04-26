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
public class ProductServiceUnitTests implements IProductServiceUnitTests {
	@Autowired
	private ProductService productService;

	@MockBean
	private ProductProducer productProducer;

	@BeforeEach
	public void setup() throws Exception {
		// Objects

		Product product1 = new Product();

		product1.setId("64441357327a68740d94ac25");
		product1.setName("Arroz Caçarola Parbolizado 1Kg");
		product1.setBarCode("17896393601012");
		product1.setPrice(4.45);
		product1.setPicture("https://supermercadojequie.com/image/cache/catalog/produtos/7896393601046-1000x1000.jpg");
		product1.setAvailable(1000);

		Product product2 = new Product();

		product2.setId("64441357327a68740d94ac26");
		product2.setName("Arroz Kika Parbolizado 1Kg");
		product2.setBarCode("7897586400026");
		product2.setPicture(
				"https://lojacentraldealimentos.com.br/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/k/i/kika30.jpg");
		product2.setPrice(4.25);
		product2.setAvailable(1000);

		Product product3 = new Product();

		product3.setId("64441357327a68740d94ac27");
		product3.setName("Arroz Urbano Parbolizado 1Kg");
		product3.setBarCode("7896038306558");
		product3.setPicture("https://statics.angeloni.com.br/super/files/produtos/629103/629103_1_zoom.jpg");
		product3.setPrice(4.75);
		product3.setAvailable(1000);

		// Create product

		Product createProductInput = new Product();

		createProductInput.setName(product2.getName());
		createProductInput.setBarCode(product2.getBarCode());
		createProductInput.setPrice(product2.getPrice());
		createProductInput.setPicture(product2.getPicture());
		createProductInput.setAvailable(product2.getAvailable());

		Product productCreated = new Product(product2);

		ServiceResponse<Product> createProductResponse = new ServiceResponse<>();

		createProductResponse.setData(productCreated);

		Mockito.when(productProducer.createProduct(createProductInput)).thenReturn(createProductResponse);

		// Create product with existing bar code

		Product createProductWithExistingBarCodeInput = new Product();

		createProductWithExistingBarCodeInput.setName(product3.getName());
		createProductWithExistingBarCodeInput.setBarCode(product1.getBarCode());
		createProductWithExistingBarCodeInput.setPrice(product3.getPrice());
		createProductWithExistingBarCodeInput.setPicture(product3.getPicture());
		createProductWithExistingBarCodeInput.setAvailable(product3.getAvailable());

		ServiceResponse<Product> createProductWithExistingBarCodeResponse = new ServiceResponse<>();

		createProductWithExistingBarCodeResponse.setErrorMessage("Já existe um produto com esse código de barras");

		Mockito.when(productProducer.createProduct(createProductWithExistingBarCodeInput))
				.thenReturn(createProductWithExistingBarCodeResponse);

		// Find all products

		List<Product> productsFound = Arrays.asList(product1, product2);

		ServiceResponse<List<Product>> findAllProductsResponse = new ServiceResponse<>();

		findAllProductsResponse.setData(productsFound);

		Mockito.when(productProducer.findAllProducts()).thenReturn(findAllProductsResponse);

		// Find product by id

		Product productFoundById = new Product(product1);

		ServiceResponse<Product> findProductByIdResponse = new ServiceResponse<>();

		findProductByIdResponse.setData(productFoundById);

		Mockito.when(productProducer.findProductById(product1.getId())).thenReturn(findProductByIdResponse);

		// Find product that does not exist by id

		ServiceResponse<Product> findProductThatDoesNotExistByIdResponse = new ServiceResponse<>();

		Mockito.when(productProducer.findProductById(product3.getId()))
				.thenReturn(findProductThatDoesNotExistByIdResponse);

		// Update product by id

		Product updateProductByIdInput = new Product();

		updateProductByIdInput.setAvailable(900);

		Product productUpdatedById = new Product(product1);

		productUpdatedById.setAvailable(900);

		ServiceResponse<Product> updateProductByIdResponse = new ServiceResponse<>();

		updateProductByIdResponse.setData(productUpdatedById);

		Mockito.when(productProducer.updateProductById(product1.getId(), updateProductByIdInput))
				.thenReturn(updateProductByIdResponse);

		// Update product with existing bar code by id

		Product updateProductWithExistingBarCodeByIdInput = new Product();

		updateProductWithExistingBarCodeByIdInput.setBarCode(product1.getBarCode());

		ServiceResponse<Product> updateProductWithExistingBarCodeByIdResponse = new ServiceResponse<>();

		updateProductWithExistingBarCodeByIdResponse.setErrorMessage("Já existe um produto com esse código de barras");

		Mockito.when(productProducer.updateProductById(product2.getId(), updateProductWithExistingBarCodeByIdInput))
				.thenReturn(updateProductWithExistingBarCodeByIdResponse);

		// Update product that does not exist by id

		ServiceResponse<Product> updateProductThatDoesNotExistByIdResponse = new ServiceResponse<>();

		Mockito.when(productProducer.updateProductById(product3.getId(), updateProductByIdInput))
				.thenReturn(updateProductThatDoesNotExistByIdResponse);

		// Delete product by id

		Product productDeletedById = new Product(productUpdatedById);

		ServiceResponse<Product> deleteProductByIdResponse = new ServiceResponse<>();

		deleteProductByIdResponse.setData(productDeletedById);

		Mockito.when(productProducer.deleteProductById(product1.getId())).thenReturn(deleteProductByIdResponse);

		// Delete product that does not exist by id

		ServiceResponse<Product> deleteProductThatDoesNotExistByIdResponse = new ServiceResponse<>();

		Mockito.when(productProducer.deleteProductById(product3.getId()))
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

		createProductInput.setName("Arroz Urbano Parbolizado 1Kg");
		createProductInput.setBarCode("17896393601012");
		createProductInput.setPicture("https://statics.angeloni.com.br/super/files/produtos/629103/629103_1_zoom.jpg");
		createProductInput.setPrice(4.75);
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
		Product productFound = this.productService.findProductById("64441357327a68740d94ac25");

		Assertions.assertNotNull(productFound);
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenFindingProductByIdAndProductDoesNotExist() throws Exception {
		Assertions.assertThrows(NotFoundException.class, () -> {
			this.productService.findProductById("64441357327a68740d94ac27");
		});
	}

	@Test
	public void shouldUpdateProductById() throws Exception {
		Product updateProductInput = new Product();

		updateProductInput.setAvailable(900);

		Product productUpdated = this.productService.updateProductById("64441357327a68740d94ac25", updateProductInput);

		Assertions.assertNotNull(productUpdated);
		Assertions.assertEquals(updateProductInput.getAvailable(), productUpdated.getAvailable());
	}

	@Test
	public void shouldThrowBadRequestExceptionWhenUpdatingProductByIdAndBarCodeAlreadyExists() throws Exception {
		Product updateProductInput = new Product();
		
		updateProductInput.setBarCode("17896393601012");
		
		Assertions.assertThrows(BadRequestException.class, () -> {
			this.productService.updateProductById("64441357327a68740d94ac26", updateProductInput);
		});
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenUpdatingProductByIdAndProductDoesNotExist() throws Exception {
		Product updateProductInput = new Product();

		updateProductInput.setAvailable(900);

		Assertions.assertThrows(NotFoundException.class, () -> {
			this.productService.updateProductById("64441357327a68740d94ac27", updateProductInput);
		});
	}


	@Test
	public void shouldDeleteProductById() throws Exception {
		Product productDeleted = this.productService.deleteProductById("64441357327a68740d94ac25");

		Assertions.assertNotNull(productDeleted);
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenDeletingProductByIdAndProductDoesNotExist() throws Exception {
		Assertions.assertThrows(NotFoundException.class, () -> {
			this.productService.deleteProductById("64441357327a68740d94ac27");
		});
	}
}
