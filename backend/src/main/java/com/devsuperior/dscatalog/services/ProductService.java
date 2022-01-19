package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.mappers.ProductMapper;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.requests.ProductRequest;
import com.devsuperior.dscatalog.responses.ProductResponse;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductResponse> findAllProducts(String name, Long categoryId, Pageable pageable) {
		List<Category> categories = (categoryId == 0) ? null : List.of(categoryRepository.getById(categoryId));
		
		Page<Product> productPage = productRepository.findAllProducts(name, categories, pageable);
		
		productRepository.findProductWithCategory(productPage.getContent());

		return productPage.map(product -> new ProductResponse(product));
	}

	@Transactional(readOnly = true)
	public ProductResponse findProductByPrimaryKey(Long id) {
		Optional<Product> optionalProduct = productRepository.findById(id);
		
		Product product = optionalProduct.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		
		return new ProductResponse(product, product.getCategories());
	}

	@Transactional
	public ProductResponse saveNewProduct(ProductRequest productRequest) {
		Product product = new Product();
		
		ProductMapper.mapperProductRequestToProduct(productRequest, product);
		helperGetCategories(productRequest, product);
		
		product = productRepository.save(product);
		
		return new ProductResponse(product);
	}

	@Transactional
	public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
		try {
			Product product = productRepository.getById(id);
			
			ProductMapper.mapperProductRequestToProduct(productRequest, product);
			helperGetCategories(productRequest, product);
			
			product = productRepository.save(product);
			
			return new ProductResponse(product);
		}catch(EntityNotFoundException ex) {
			throw new ResourceNotFoundException("Product by id " + id + " does not exist");
		}
	}

	public void deleteProductByPrimaryKey(Long id) {
		try {
			productRepository.deleteById(id);
		} catch(EmptyResultDataAccessException ex) {
			throw new ResourceNotFoundException("Product by id " + id + " not found");
		} catch(DataIntegrityViolationException ex) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void helperGetCategories(ProductRequest productRequest, Product product) {
		product.getCategories().clear();
		
		for(Long id : productRequest.getCategories()) {
			Category category = categoryRepository.getById(id);
			
			product.getCategories().add(category);
		}
	}
 }
