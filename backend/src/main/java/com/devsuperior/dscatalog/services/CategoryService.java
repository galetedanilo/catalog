package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.mappers.CategoryMapper;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.requests.CategoryRequest;
import com.devsuperior.dscatalog.responses.CategoryResponse;
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
import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private final CategoryRepository categoryRepository;
		
	@Transactional(readOnly = true)
	public Page<CategoryResponse> findAllCategories(Pageable pageable) {
		Page<Category> categoryPage = categoryRepository.findAll(pageable);
		
		return categoryPage.map(x -> new CategoryResponse(x));
	}

	@Transactional(readOnly = true)
	public CategoryResponse findCategoryByPrimaryKey(Long id) {
		Optional<Category> categoryOptional = categoryRepository.findById(id);
		
		Category category = categoryOptional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		
		return new CategoryResponse(category);
	}

	@Transactional
	public CategoryResponse saveNewCategory(CategoryRequest categoryRequest) {
		Category category = new Category();

		CategoryMapper.mapperCategoryRequestToCategory(categoryRequest, category);

		category.setCreatedAt(Instant.now());
		
		category = categoryRepository.save(category);
		
		return new CategoryResponse(category);
	}

	@Transactional
	public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
		try {
			Category category = categoryRepository.getById(id);

			CategoryMapper.mapperCategoryRequestToCategory(categoryRequest, category);

			category.setUpdatedAt(Instant.now());

			category = categoryRepository.save(category);

			return new CategoryResponse(category);
		}catch(EntityNotFoundException ex) {
			throw new ResourceNotFoundException("Category by id " + id + " does not exist");
		}
	}

	public void deleteCategoryByPrimaryKey(Long id) {
		try {
			categoryRepository.deleteById(id);
		} catch(EmptyResultDataAccessException ex) {
			throw new ResourceNotFoundException("Category by id " + id + " not found");
		} catch(DataIntegrityViolationException ex) {
			throw new DatabaseException("Integrity violation");
		}
	}
 }
