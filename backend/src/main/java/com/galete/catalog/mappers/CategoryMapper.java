package com.galete.catalog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.galete.catalog.entities.Category;
import com.galete.catalog.entities.projections.CategoryProjection;
import com.galete.catalog.requests.CategoryRequest;
import com.galete.catalog.responses.CategoryMinResponse;
import com.galete.catalog.responses.CategoryResponse;

@Mapper
public interface CategoryMapper {
	
	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
	
	@Mapping(source = "id", target = "categoryId")
	CategoryResponse categoryToCategoryResponse(Category entity);
	
	CategoryMinResponse categoryProjectionToCategoryMinResponse(CategoryProjection projection);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "created", ignore = true)
	@Mapping(target = "updated", ignore = true)
	@Mapping(target = "products", ignore = true)
	Category categoryRequestToCategory(CategoryRequest request);

}
