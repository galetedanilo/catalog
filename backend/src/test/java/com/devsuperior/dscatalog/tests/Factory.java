package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.requests.ProductRequest;
import com.devsuperior.dscatalog.responses.ProductResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good Phone",
				BigDecimal.valueOf(800.00), "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));

		product.getCategories().add(new Category(1L, "Electronics"));
		
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

		return new Category(1L, "Electronic", Instant.now(), Instant.now());

	}
}
