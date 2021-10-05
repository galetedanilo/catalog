package com.galete.catalog.requests;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String description;
	
	private BigDecimal price;
	
	private String imageUri;
	
	private Instant date;
	
	@Valid
	private Set<CategoryMinRequest> categories;
}