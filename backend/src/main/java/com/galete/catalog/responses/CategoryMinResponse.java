package com.galete.catalog.responses;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMinResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
}
