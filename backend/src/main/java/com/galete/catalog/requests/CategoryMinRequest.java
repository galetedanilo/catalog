package com.galete.catalog.requests;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMinRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message = "The category id field is required")
	private Long id;
	
	@NotBlank(message = "The name field is required")
	@Size(min = 3, max = 50, message = "The name field must be between 3 and 50 characters")
	private String name;
}
