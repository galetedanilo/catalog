package com.galete.catalog.services;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.galete.catalog.entities.Product;
import com.galete.catalog.mappers.ProductMapper;
import com.galete.catalog.repositories.ProductRepository;
import com.galete.catalog.requests.ProductRequest;
import com.galete.catalog.responses.ProductResponse;
import com.galete.catalog.services.exceptions.DatabaseException;
import com.galete.catalog.services.exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final ProductRepository productRepository;
	
	private final ProductMapper productMapper = ProductMapper.INSTANCE;
	
	@Transactional(readOnly = true)
	public Page<ProductResponse> findAllProducts(Pageable pageable) {
		
		Page<Product> page = productRepository.findAll(pageable);
		
		return page.map(obj -> productMapper.productToProductResponse(obj));
	}
	
	@Transactional(readOnly = true)
	public ProductResponse findProductById(Long id) {
		
		Optional<Product> optional = productRepository.findById(id);
		
		Product entity = optional.orElseThrow(() -> new ResourceNotFoundException(""));
		
		return productMapper.productToProductResponse(entity);
	}
	
	public ProductResponse addProduct(ProductRequest request) {
		
		if(productRepository.findByName(request.getName()).isPresent()) {
			throw new DatabaseException("Product with name " + request.getName() + " is already exist");
		}
		
		Product entity = productMapper.productRequestToProduct(request);
		
		entity.setCreated(Instant.now());
		entity.setUpdated(Instant.now());
		
		entity = productRepository.save(entity);
		
		return productMapper.productToProductResponse(entity);
	}
	
	public void deleteProduct(Long id) {
		try {
			productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Resource not found in database with id: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
}
