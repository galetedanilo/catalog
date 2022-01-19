package com.devsuperior.dscatalog.requests;

import lombok.*;

import javax.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Size(min = 4, max = 50, message = "Name must be between 4 to 50 characters")
    @NotBlank(message = "Name is required")
    private String name;

    @Size(min = 5, max = 200, message = "Description must be between 5 to 200 characters")
    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "O preço deve ser um valor positivo")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price of being a value greater than zero")
    @Digits(integer = 6, fraction = 2, message = "Integer")
    private BigDecimal price;

    @Size(min = 5, max = 100, message = "Image url must be between 5 to 100 characters")
    @NotBlank(message = "Image url is required")
    private String imgUrl;

    @PastOrPresent(message = "A data não pode ser futuro")
    private Instant date;

    private List<Long> categories = new ArrayList<>();
}
