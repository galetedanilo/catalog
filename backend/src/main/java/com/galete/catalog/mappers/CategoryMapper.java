package com.galete.catalog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.galete.catalog.entities.Category;
import com.galete.catalog.entities.projections.CategoryProjection;
import com.galete.catalog.requests.CategoryRequest;
import com.galete.catalog.responses.CategoryMinResponse;
import com.galete.catalog.responses.CategoryResponse;

@Mapper
public interface CategoryMapper {
	
	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
	
	CategoryResponse categoryToCategoryResponse(Category entity);
	
	CategoryMinResponse categoryProjectionToCategoryMinResponse(CategoryProjection projection);
	
	Category categoryRequestToCategory(CategoryRequest request);

}
