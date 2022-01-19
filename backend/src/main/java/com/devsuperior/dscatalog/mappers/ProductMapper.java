package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.requests.ProductRequest;

import java.io.Serial;
import java.io.Serializable;

public class ProductMapper implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static void mapperProductRequestToProduct(ProductRequest productRequest, Product product) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setImgUrl(productRequest.getImgUrl());
        product.setDate(productRequest.getDate());
    }
}
