package com.sparks.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sparks.api.entities.Product;
import com.sparks.api.exceptions.BadRequestException;
import com.sparks.api.exceptions.NotFoundException;
import com.sparks.api.producers.ProductProducer;
import com.sparks.api.responses.ServiceResponse;

@Service
public class ProductService {
	@Autowired
	private ProductProducer productProducer;

	public Product createProduct(Product createProductInput) throws Exception {
		ServiceResponse<Product> response = this.productProducer.createProduct(createProductInput);
		
		if (response.getErrorMessage() != null) {
			throw new BadRequestException(response.getErrorMessage());
		}
		
		return response.getData();
	}

	public List<Product> findAllProducts() throws Exception {
		ServiceResponse<List<Product>> response = this.productProducer.findAllProducts();
		
		if (response.getErrorMessage() != null) {
			throw new BadRequestException(response.getErrorMessage());
		}
		
		return response.getData();
	}

	public Product findProductById(String id) throws Exception {
		ServiceResponse<Product> response = this.productProducer.findProductById(id);
		
		if (response.getErrorMessage() != null) {
			throw new BadRequestException(response.getErrorMessage());
		}
		
		if (response.getData() == null) {
			throw new NotFoundException("Produto não encontrado");
		}
		
		return response.getData();
	}

	public Product updateProductById(String id, Product updateProductInput) throws Exception {
		ServiceResponse<Product> response = this.productProducer.updateProductById(id, updateProductInput);
		
		if (response.getErrorMessage() != null) {
			throw new BadRequestException(response.getErrorMessage());
		}
		
		if (response.getData() == null) {
			throw new NotFoundException("Produto não encontrado");
		}
		
		return response.getData();
	}

	public Product deleteProductById(String id) throws Exception {
		ServiceResponse<Product> response = this.productProducer.deleteProductById(id);
		
		if (response.getErrorMessage() != null) {
			throw new BadRequestException(response.getErrorMessage());
		}
		
		if (response.getData() == null) {
			throw new NotFoundException("Produto não encontrado");
		}
		
		return response.getData();
	}
}
