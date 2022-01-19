package com.devsuperior.dscatalog.controllers;

import java.net.URI;

import javax.validation.Valid;

import com.devsuperior.dscatalog.requests.ProductRequest;
import com.devsuperior.dscatalog.responses.ProductResponse;
import lombok.AllArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.services.ProductService;

@RestController
@RequestMapping(value = "/api/v1/products")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;
	
	@GetMapping
	public ResponseEntity<Page<ProductResponse>> findAllProducts(
			@RequestParam(value = "category", defaultValue = "0") Long category,
			@RequestParam(value = "name", defaultValue = "") String name,
			Pageable pageable) {
		
		Page<ProductResponse> productResponsePage = productService.findAllProducts(name.trim(), category, pageable);
		
		return ResponseEntity.ok().body(productResponsePage);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductResponse> findProductByPrimaryKey(@PathVariable Long id) {
		ProductResponse productResponse= productService.findProductByPrimaryKey(id);
		
		return ResponseEntity.ok().body(productResponse);
	}
	
	@PostMapping
	public ResponseEntity<ProductResponse> saeNewProduct(@Valid @RequestBody ProductRequest productRequest) {
		ProductResponse productResponse = productService.saveNewProduct(productRequest);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
									.buildAndExpand(productResponse.getId()).toUri();
		
		return ResponseEntity.created(uri).body(productResponse);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
		ProductResponse productResponse = productService.updateProduct(id, productRequest);
		
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteProductByPrimaryKey(@PathVariable Long id) {
		productService.deleteProductByPrimaryKey(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
