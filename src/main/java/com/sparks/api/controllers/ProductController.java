package com.sparks.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparks.api.entities.Product;
import com.sparks.api.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	@PostMapping
	public Product createProduct(@RequestBody() Product createProductInput) throws Exception {
		return this.productService.createProduct(createProductInput);
	}

	@GetMapping
	public List<Product> findAllProducts() throws Exception {
		return this.productService.findAllProducts();
	}

	@GetMapping("/{id}")
	public Product findProductById(@PathVariable("id") String id) throws Exception {
		return this.productService.findProductById(id);
	}

	@PutMapping("/{id}")
	public Product updateProductById(@PathVariable("id") String id, @RequestBody Product updateProductInput) throws Exception {
		return this.productService.updateProductById(id, updateProductInput);
	}

	@DeleteMapping("/{id}")
	public Product deleteProductById(@PathVariable("id") String id) throws Exception {
		return this.productService.deleteProductById(id);
	}
}
