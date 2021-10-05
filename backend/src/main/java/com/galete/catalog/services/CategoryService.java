package com.galete.catalog.services;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.galete.catalog.entities.Category;
import com.galete.catalog.entities.projections.CategoryProjection;
import com.galete.catalog.mappers.CategoryMapper;
import com.galete.catalog.repositories.CategoryRepository;
import com.galete.catalog.requests.CategoryRequest;
import com.galete.catalog.responses.CategoryMinResponse;
import com.galete.catalog.responses.CategoryResponse;
import com.galete.catalog.services.exceptions.DatabaseException;
import com.galete.catalog.services.exceptions.ResourceNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final CategoryRepository categoryRepository;
	
	private final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;
	
	@Transactional(readOnly = true)
	public List<CategoryMinResponse >listAllCategories() {
		List<CategoryProjection> projection = categoryRepository.listAllCategories();
		
		return projection.stream().map(obj -> categoryMapper.categoryProjectionToCategoryMinResponse(obj))
												.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public Page<CategoryResponse> findAllCategories(Pageable pageable) {
		Page<Category> page = categoryRepository.findAll(pageable);
		
		return page.map(obj -> categoryMapper.categoryToCategoryResponse(obj));
	}
	
	@Transactional(readOnly = true)
	public CategoryResponse findCategoryById(Long id) {
		Optional<Category> optional = categoryRepository.findById(id);
		
		Category entity = optional.orElseThrow(() -> new ResourceNotFoundException("Resource not found id"));
		
		return categoryMapper.categoryToCategoryResponse(entity);		
	}
	
	@Transactional
	public CategoryResponse addCategory(CategoryRequest request) {
		
		if(categoryRepository.findByName(request.getName()).isPresent()) {
			throw new DatabaseException("Category with name " + request.getName() + " is already exist");
		}
		
		Category entity = categoryMapper.categoryRequestToCategory(request);
		
		entity.setCreated(Instant.now());
		entity.setUpdated(Instant.now());
		
		entity = categoryRepository.save(entity);
		
		return categoryMapper.categoryToCategoryResponse(entity);
	}
	
	@Transactional
	public CategoryResponse updateCategory(Long id, CategoryRequest request) {
		
		try {
			Category entity = categoryRepository.getById(id);
			
			entity = categoryMapper.categoryRequestToCategory(request);
			
			entity.setUpdated(Instant.now());
			
			entity = categoryRepository.save(entity);
			
			return categoryMapper.categoryToCategoryResponse(entity);
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Resource not found in database with id: " + id);
		}
	}
	
	public void deleteCategory(Long id) {
		try {
			categoryRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Resource not found in database with id: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
}
