package com.galete.catalog.responses;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse extends RepresentationModel<ProductResponse> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long productId;
	
	private String name;
	
	private String description;
	
	private BigDecimal price;
	
	private Instant date;
	
	private Instant creaded;
	
	private Instant updated;
	
	private String imageUri;
	
	private Set<CategoryMinResponse> categories;
}
