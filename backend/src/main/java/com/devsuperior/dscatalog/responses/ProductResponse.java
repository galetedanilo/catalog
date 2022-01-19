package com.devsuperior.dscatalog.responses;

import com.devsuperior.dscatalog.controllers.ProductController;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends RepresentationModel<ProductResponse> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imgUrl;
    private Instant date;
    private List<CategoryResponse> categories = new ArrayList<>();

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imgUrl = product.getImgUrl();
        this.date = product.getDate();

        this.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).findProductByPrimaryKey(id))
                .withSelfRel()
        );

        this.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).deleteProductByPrimaryKey(id))
                .withRel("Delete product")
        );

        this.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ProductController.class).findAllProducts(0L, "", PageRequest.of(0, 20)))
                .withRel("Find all products")
        );
    }

    public ProductResponse(Product product, Set<Category> categories) {
        this(product);
        categories.forEach(category -> this.categories.add(new CategoryResponse(category)));
    }
}
