package com.galete.catalog.controllers;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.galete.catalog.requests.CategoryRequest;
import com.galete.catalog.responses.CategoryResponse;
import com.galete.catalog.services.CategoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/categories")
@AllArgsConstructor
public class CategoryController {

	public final CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<Page<CategoryResponse>> findAllCategories(Pageable pageable) {
		
		Page<CategoryResponse> response = categoryService.findAllCategories(pageable);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryResponse> findCagegoryById(@PathVariable Long id) {
		
		CategoryResponse response = categoryService.findCategoryById(id);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<CategoryResponse> addCategory(@Valid @RequestBody CategoryRequest request) {
		
		CategoryResponse response = categoryService.addCategory(request);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
	
		CategoryResponse response = categoryService.updateCategory(id, request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		
		categoryService.deleteCategory(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
