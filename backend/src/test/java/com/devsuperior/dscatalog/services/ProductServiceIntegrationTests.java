package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.responses.ProductResponse;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTests {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	public void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void findAllProductsShouldReturnSortedPageWhenShortByName() {
		
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		
		Page<ProductResponse> productResponsePage = productService.findAllProducts("", 0L, pageRequest);
		
		Assertions.assertFalse(productResponsePage.isEmpty());

		Assertions.assertEquals("Macbook Pro", productResponsePage.getContent().get(0).getName());
	}
	
	@Test
	public void findAllProductsShouldReturnEmptyPageWhenPageDoesNotExisting() {
		
		PageRequest pageRequest = PageRequest.of(50, 10);
		
		Page<ProductResponse> productResponsePage = productService.findAllProducts("", 0L, pageRequest);
		
		Assertions.assertTrue(productResponsePage.isEmpty());
	}
	
	@Test
	public void findAllProductsShouldReturnPageWhenPageZeroSizeTen() {
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ProductResponse> productResponsePage = productService.findAllProducts("", 0L, pageRequest);
		
		Assertions.assertFalse(productResponsePage.isEmpty());
		Assertions.assertEquals(0, productResponsePage.getNumber());
		Assertions.assertEquals(10, productResponsePage.getSize());
		Assertions.assertEquals(countTotalProducts, productResponsePage.getTotalElements());
	}
	
	@Test
	public void deleteProductShouldDeleteResourceWhenIdExists() {
		
		productService.deleteProductByPrimaryKey(existingId);
		
		Assertions.assertEquals(countTotalProducts - 1, productRepository.count());
	}
	
	@Test
	public void deleteProductShouldThrowResourceNotFoundExceptionWhenIdDoesNotExisting() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.deleteProductByPrimaryKey(nonExistingId);
		});
	}

}
