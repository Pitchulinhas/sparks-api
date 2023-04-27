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
public class ProductControllerUnitTests implements IProductControllerTests {
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

		Mockito.when(productService.createProduct(createProductInput)).thenReturn(productCreated);

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

		Mockito.when(productService.createProduct(createProductWithExistingBarCodeInput))
				.thenThrow(new BadRequestException("Já existe um produto com esse código de barras"));

		///////////////////////
		// Find all products //
		///////////////////////

		List<Product> productsFound = Arrays.asList(previouslyCreatedProduct, productCreated);

		Mockito.when(productService.findAllProducts()).thenReturn(productsFound);

		////////////////////////
		// Find product by id //
		////////////////////////

		Product productFoundById = new Product(productCreated);

		Mockito.when(productService.findProductById(productCreated.getId())).thenReturn(productFoundById);

		////////////////////////////////////////////
		// Find product that does not exist by id //
		////////////////////////////////////////////

		Mockito.when(productService.findProductById("64441357327a68740d94ac28"))
				.thenThrow(new NotFoundException("Produto não encontrado"));

		//////////////////////////
		// Update product by id //
		//////////////////////////

		Product updateProductByIdInput = new Product();

		updateProductByIdInput.setAvailable(900);

		Product productUpdatedById = new Product(productCreated);

		productUpdatedById.setAvailable(900);

		Mockito.when(productService.updateProductById(productCreated.getId(), updateProductByIdInput))
				.thenReturn(productUpdatedById);

		/////////////////////////////////////////////////
		// Update product with existing bar code by id //
		/////////////////////////////////////////////////

		Product updateProductWithExistingBarCodeByIdInput = new Product();

		updateProductWithExistingBarCodeByIdInput.setBarCode(previouslyCreatedProduct.getBarCode());

		Mockito.when(
				productService.updateProductById(productCreated.getId(), updateProductWithExistingBarCodeByIdInput))
				.thenThrow(new BadRequestException("Já existe um produto com esse código de barras"));

		//////////////////////////////////////////////
		// Update product that does not exist by id //
		//////////////////////////////////////////////

		Product updateProductThatDoesNotExistByIdInput = new Product();

		updateProductThatDoesNotExistByIdInput.setAvailable(900);

		Mockito.when(
				productService.updateProductById("64441357327a68740d94ac28", updateProductThatDoesNotExistByIdInput))
				.thenThrow(new NotFoundException("Produto não encontrado"));

		//////////////////////////
		// Delete product by id //
		//////////////////////////

		Product productDeletedById = new Product(productUpdatedById);

		Mockito.when(productService.deleteProductById(productCreated.getId())).thenReturn(productDeletedById);

		//////////////////////////////////////////////
		// Delete product that does not exist by id //
		//////////////////////////////////////////////

		Mockito.when(productService.deleteProductById("64441357327a68740d94ac28"))
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

		createProductInput.setName("Arroz Kika Parbolizado 1Kg");
		createProductInput.setBarCode("7897586400026");
		createProductInput.setPicture(
				"https://lojacentraldealimentos.com.br/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/k/i/kika30.jpg");
		createProductInput.setPrice(4.35);
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
		this.mockMvc.perform(get("/products/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldReturnNotFoundStatusCodeWhenFindingProductByIdAndProductDoesNotExist() throws Exception {
		this.mockMvc.perform(get("/products/{id}", "64441357327a68740d94ac28").contentType(MediaType.APPLICATION_JSON))
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
				.perform(put("/products/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateProductInput)))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.available", is(900)));
	}

	@Test
	public void shouldReturnBadRequestStatusCodeWhenUpdatingProductByIdAndBarCodeAlreadyExists() throws Exception {
		Product updateProductInput = new Product();

		updateProductInput.setBarCode("17896393601012");

		this.mockMvc
				.perform(put("/products/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON)
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
				.perform(put("/products/{id}", "64441357327a68740d94ac28").contentType(MediaType.APPLICATION_JSON)
						.content(gson.toJson(updateProductInput)))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Produto não encontrado")));
	}

	@Test
	public void shouldDeleteProductById() throws Exception {
		this.mockMvc
				.perform(delete("/products/{id}", "64441357327a68740d94ac27").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void shouldReturnNotFoundStatusCodeWhenDeletingProductByIdAndProductDoesNotExist() throws Exception {
		this.mockMvc
				.perform(delete("/products/{id}", "64441357327a68740d94ac28").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.error", notNullValue()))
				.andExpect(jsonPath("$.error", is("Produto não encontrado")));
		;
	}
}
