package com.galete.catalog.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.galete.catalog.entities.Product;
import com.galete.catalog.requests.ProductRequest;
import com.galete.catalog.responses.ProductResponse;

@Mapper(uses = {CategoryMapper.class})
public interface ProductMapper {

	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
	
	ProductResponse productToProductResponse(Product entity);
	
	Product productRequestToProduct(ProductRequest request);
}