package com.galete.catalog.responses;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse extends RepresentationModel<CategoryResponse> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long categoryId;
	
	private String name;
	
	private Instant created;
	
	private Instant updated;
}
