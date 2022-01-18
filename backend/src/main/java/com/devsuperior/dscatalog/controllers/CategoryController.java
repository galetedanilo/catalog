package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.requests.CategoryRequest;
import com.devsuperior.dscatalog.responses.CategoryResponse;
import com.devsuperior.dscatalog.services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/categories")
@AllArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<Page<CategoryResponse>> findAllCategories(Pageable pageable) {
				
		Page<CategoryResponse> categoryResponsePage = categoryService.findAllCategories(pageable);
		
		return ResponseEntity.ok().body(categoryResponsePage);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryResponse> findCategoryByPrimaryKey(@PathVariable Long id) {
		CategoryResponse categoryResponse = categoryService.findCategoryByPrimaryKey(id);
		
		return ResponseEntity.ok().body(categoryResponse);
	}
	
	@PostMapping
	public ResponseEntity<CategoryResponse> saveNewCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
		CategoryResponse categoryResponse = categoryService.saveNewCategory(categoryRequest);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
									.buildAndExpand(categoryResponse.getId()).toUri();
		
		return ResponseEntity.created(uri).body(categoryResponse);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
		CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest);
		
		return ResponseEntity.ok().body(categoryResponse);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteCategoryByPrimaryKey(@PathVariable Long id) {
		categoryService.deleteCategoryByPrimaryKey(id);
		
		//retorna código HTTP 204: resposta deu certo corpo vazio
		return ResponseEntity.noContent().build();
	}
}
