package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.requests.ProductRequest;
import com.devsuperior.dscatalog.responses.ProductResponse;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService productService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;

	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductRequest productRequest;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		product = Factory.createProduct();
		productRequest = Factory.createProductRequest();
		category = Factory.createCategory();
		
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(productRepository.findAllProducts(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(productRepository.getById(existingId)).thenReturn(product);
		Mockito.when(productRepository.getById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getById(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.doNothing().when(productRepository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
	}
	
	@Test
	public void updateShouldReturnProductWhenIdExists() {

		ProductResponse productResponse = productService.updateProduct(existingId, productRequest);
		
		Assertions.assertNotNull(productResponse);
	}
	
	@Test
	public void updateProductShouldThrowResourceNotFoundExceptionWhenIdDoesNotExisting() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.updateProduct(nonExistingId, productRequest);
		});
	}
	
	@Test
	public void findProductByPrimaryKeyShouldReturnProductDTOWhenIdExists() {
		
		ProductResponse productResponse = productService.findProductByPrimaryKey(existingId);
		
		Assertions.assertNotNull(productResponse);
		Mockito.verify(productRepository, Mockito.times(1)).findById(existingId);
		
	}
	
	@Test
	public void findProductByPrimaryKeyShouldThrowResourceNotFoundExceptionWhenIdDoesNotExisting() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
		
			productService.findProductByPrimaryKey(nonExistingId);
		});
		
		Mockito.verify(productRepository, Mockito.times(1)).findById(nonExistingId);
	}
	
	@Test
	public void findAllProductsShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductResponse> result = productService.findAllProducts("", 0L,pageable);
		
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void deleteProductShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			productService.deleteProductByPrimaryKey(dependentId);
		});
		
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(dependentId);
	}
	
	@Test
	public void deleteProductShouldThrowResourceNotFoundExceptionWhenIdDoesNotExisting() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.deleteProductByPrimaryKey(nonExistingId);
		});
		
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteProductShouldDoNothingWhenIdExists() {
				
		Assertions.assertDoesNotThrow(() -> {
			productService.deleteProductByPrimaryKey(existingId);
		});
		
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
		
	}
}
