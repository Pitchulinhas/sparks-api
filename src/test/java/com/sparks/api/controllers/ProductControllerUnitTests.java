package com.sparks.api.controllers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.sparks.api.entities.Product;
import com.sparks.api.exceptions.BadRequestException;
import com.sparks.api.exceptions.NotFoundException;
import com.sparks.api.services.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerUnitTests implements IProductControllerUnitTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	private Gson gson;

	public ProductControllerUnitTests() {
		this.gson = new Gson();
	}

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

		Mockito.when(productService.createProduct(createProductInput)).thenReturn(productCreated);

		// Create product with existing bar code

		Product createProductWithExistingBarCodeInput = new Product();

		createProductWithExistingBarCodeInput.setName(product3.getName());
		createProductWithExistingBarCodeInput.setBarCode(product1.getBarCode());
		createProductWithExistingBarCodeInput.setPrice(product3.getPrice());
		createProductWithExistingBarCodeInput.setPicture(product3.getPicture());
		createProductWithExistingBarCodeInput.setAvailable(product3.getAvailable());

		Mockito.when(productService.createProduct(createProductWithExistingBarCodeInput))
				.thenThrow(new BadRequestException("Já existe um produto com esse código de barras"));

		// Find all products

		List<Product> productsFound = Arrays.asList(product1, product2);

		Mockito.when(productService.findAllProducts()).thenReturn(productsFound);

		// Find product by id

		Product productFoundById = new Product(product1);

		Mockito.when(productService.findProductById(product1.getId())).thenReturn(productFoundById);

		// Find product that does not exist by id

		Mockito.when(productService.findProductById(product3.getId()))
				.thenThrow(new NotFoundException("Produto não encontrado"));

		// Update product by id

		Product updateProductByIdInput = new Product();

		updateProductByIdInput.setAvailable(900);

		Product productUpdatedById = new Product(product1);

		productUpdatedById.setAvailable(900);

		Mockito.when(productService.updateProductById(product1.getId(), updateProductByIdInput))
				.thenReturn(productUpdatedById);

		// Update product with existing bar code by id

		Product updateProductWithExistingBarCodeByIdInput = new Product();

		updateProductWithExistingBarCodeByIdInput.setBarCode(product1.getBarCode());

		Mockito.when(productService.updateProductById(product2.getId(), updateProductWithExistingBarCodeByIdInput))
				.thenThrow(new BadRequestException("Já existe um produto com esse código de barras"));

		// Update product that does not exist by id

		Mockito.when(productService.updateProductById(product3.getId(), updateProductByIdInput))
				.thenThrow(new NotFoundException("Produto não encontrado"));

		// Delete product by id

		Product productDeletedById = new Product(productUpdatedById);

		Mockito.when(productService.deleteProductById(product1.getId())).thenReturn(productDeletedById);

		// Delete product that does not exist by id

		Mockito.when(productService.deleteProductById(product3.getId()))
				.thenThrow(new NotFoundException("Produto não encontrado"));

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

		this.mockMvc
				.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(createProductInput)))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldReturnBadRequestStatusCodeWhenCreatingProductAndBarCodeAlreadyExists() throws Exception {
		Product createProductInput = new Product();

		createProductInput.setName("Arroz Urbano Parbolizado 1Kg");
		createProductInput.setBarCode("17896393601012");
		createProductInput.setPicture("https://statics.angeloni.com.br/super/files/produtos/629103/629103_1_zoom.jpg");
		createProductInput.setPrice(4.75);
		createProductInput.setAvailable(1000);

		this.mockMvc
				.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(createProductInput)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Já existe um produto com esse código de barras")));
	}

	@Test
	public void shouldFindAllProducts() throws Exception {
		this.mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void shouldFindProductById() throws Exception {
		this.mockMvc.perform(get("/products/{id}", "64441357327a68740d94ac25").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldReturnNotFoundStatusCodeWhenFindingProductByIdAndProductDoesNotExist() throws Exception {
		this.mockMvc.perform(get("/products/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Produto não encontrado")));
		;
	}

	@Test
	public void shouldUpdateProductById() throws Exception {
		Product updateProductInput = new Product();

		updateProductInput.setAvailable(900);

		this.mockMvc
				.perform(put("/products/{id}", "64441357327a68740d94ac25").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateProductInput)))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.available", is(900)));
	}

	@Test
	public void shouldReturnBadRequestStatusCodeWhenUpdatingProductByIdAndBarCodeAlreadyExists() throws Exception {
		Product updateProductInput = new Product();

		updateProductInput.setBarCode("17896393601012");

		this.mockMvc
				.perform(put("/products/{id}", "64441357327a68740d94ac26").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateProductInput)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Já existe um produto com esse código de barras")));
	}

	@Test
	public void shouldReturnNotFoundStatusCodeWhenUpdatingProductByIdAndProductDoesNotExist() throws Exception {
		Product updateProductInput = new Product();

		updateProductInput.setAvailable(900);

		this.mockMvc
				.perform(put("/products/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateProductInput)))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Produto não encontrado")));
	}

	@Test
	public void shouldDeleteProductById() throws Exception {
		this.mockMvc
				.perform(delete("/products/{id}", "64441357327a68740d94ac25").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldReturnNotFoundStatusCodeWhenDeletingProductByIdAndProductDoesNotExist() throws Exception {
		this.mockMvc
				.perform(delete("/products/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Produto não encontrado")));
		;
	}
}
