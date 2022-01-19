package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 100L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void findProductByPrimaryKeyShouldReturnNonEmptyOptionalWhenIdExists() {
		
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findProductByPrimaryKeyShouldReturnEmptyOptionalWhenIdDoesNotExisting() {

		Optional<Product> product = repository.findById(nonExistingId);
		
		Assertions.assertTrue(product.isEmpty());
	}
	
	@Test
	public void saveNewProductShouldPersistWithAutoincrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		
		Assertions.assertEquals(product.getId(), countTotalProducts + 1);
	}

	@Test
	public void deleteProductShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteProductcShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExisting() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		});
	}	
}
