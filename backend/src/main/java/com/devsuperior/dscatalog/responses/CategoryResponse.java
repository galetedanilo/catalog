package com.devsuperior.dscatalog.responses;

import com.devsuperior.dscatalog.controllers.CategoryController;
import com.devsuperior.dscatalog.entities.Category;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.io.Serializable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse extends RepresentationModel<CategoryResponse> implements Serializable {

    private Long id;
    private String name;

    public CategoryResponse(Category category) {
        id = category.getId();
        name = category.getName();

        this.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).findCategoryByPrimaryKey(id))
                .withSelfRel()
        );
        
        this.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).deleteCategoryByPrimaryKey(id))
                .withRel("Delete category")
        );

        this.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(CategoryController.class).findAllCategories(PageRequest.of(0, 20)))
                .withRel("Find all categories")
        );
    }
}
