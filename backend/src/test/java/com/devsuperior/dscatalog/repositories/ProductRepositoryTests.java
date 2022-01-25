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
	private ProductRepository productRepository;
	
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
		
		Optional<Product> result = productRepository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findProductByPrimaryKeyShouldReturnEmptyOptionalWhenIdDoesNotExisting() {

		Optional<Product> product = productRepository.findById(nonExistingId);
		
		Assertions.assertTrue(product.isEmpty());
	}
	
	@Test
	public void saveNewProductShouldPersistWithAutoincrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();

		product.setId(null);
		
		product = productRepository.save(product);

		Optional<Product> result = productRepository.findById(product.getId());

		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1L, product.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(result.get(), product);
	}

	@Test
	public void deleteProductShouldDeleteObjectWhenIdExists() {
		
		productRepository.deleteById(existingId);
		
		Optional<Product> result = productRepository.findById(existingId);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteProductcShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExisting() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			productRepository.deleteById(nonExistingId);
		});
	}	
}
