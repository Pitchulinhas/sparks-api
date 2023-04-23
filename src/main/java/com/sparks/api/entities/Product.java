package com.sparks.api.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {
	private String id;
	private String name;
	private Float price;
	private String picture;
	private Integer available;

	public Product(Product product) {
		this.id = product.id;
		this.name = product.name;
		this.price = product.price;
		this.picture = product.picture;
		this.available = product.available;
	}
}
