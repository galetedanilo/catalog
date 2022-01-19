package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.requests.ProductRequest;
import com.devsuperior.dscatalog.responses.ProductResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;

public class Factory {

	public static Product createProduct() {
		Product product = new Product();
//		Product product = Product.builder()
//				.id(1L)
//				.name("Phone")
//				.description("Good Phone")
//				.price(BigDecimal.valueOf(800.00))
//				.imgUrl("https://img.com/img.png")
//				.date(Instant.parse("2020-10-20T03:00:00Z"))
//				.categories(new HashSet<>())
//				.build();

		product.getCategories().add(createCategory());
		
		return product;
	}

	public static ProductRequest createProductRequest() {
		ProductRequest product = ProductRequest.builder()
				.name("Phone")
				.description("Good Phone")
				.price(BigDecimal.valueOf(800.00))
				.imgUrl("https://img.com/img.png")
				.date(Instant.parse("2020-10-20T03:00:00Z"))
				.categories(new ArrayList<>())
				.build();

		product.getCategories().add(1L);

		return product;
	}

	public static ProductResponse createProductResponse() {
		ProductResponse productResponse = new ProductResponse(createProduct());

		return productResponse;
	}

	public static Category createCategory() {

		return new Category();
//		return Category.builder()
//				.id(1L)
//				.name("Electronic")
//				.createdAt(Instant.now())
//				.updatedAt(Instant.now())
//				.build();
	}
}
