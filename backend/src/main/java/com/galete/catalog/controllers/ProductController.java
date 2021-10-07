package com.galete.catalog.controllers;

import java.net.URI;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.galete.catalog.requests.ProductRequest;
import com.galete.catalog.responses.ProductResponse;
import com.galete.catalog.services.ProductService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/products")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;
	
	@GetMapping
	public ResponseEntity<Page<ProductResponse>> findAllProducts(Pageable pageable) {
		
		Page<ProductResponse> response = productService.findAllProducts(pageable);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductResponse> findProductById(@PathVariable Long id) {
		
		ProductResponse response = productService.findProductById(id);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request) {
		
		ProductResponse response = productService.addProduct(request);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
					.buildAndExpand(response.getProductId()).toUri();
		
		return ResponseEntity.created(uri).body(response);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
		
		ProductResponse response = productService.updateProduct(id, request);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
