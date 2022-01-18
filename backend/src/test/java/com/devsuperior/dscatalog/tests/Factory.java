package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;
import java.util.HashSet;

public class Factory {

	public static Product createProduct() {
		Product product = Product.builder()
				.id(1L)
				.name("Phone")
				.description("Good Phone")
				.price(800.0)
				.imgUrl("https://img.com/img.png")
				.date(Instant.parse("2020-10-20T03:00:00Z"))
				.categories(new HashSet<>())
				.build();

		product.getCategories().add(createCategory());
		
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createCategory() {
		return Category.builder()
				.id(1L)
				.name("Electronic")
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();
	}
}
