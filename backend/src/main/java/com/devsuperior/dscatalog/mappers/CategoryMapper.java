package com.devsuperior.dscatalog.mappers;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.requests.CategoryRequest;

import java.io.Serial;
import java.io.Serializable;

public class CategoryMapper implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static void mapperCategoryRequestToCategory(CategoryRequest categoryRequest, Category category) {
        category.setName(categoryRequest.getName());
    }
}
