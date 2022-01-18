package com.devsuperior.dscatalog.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest implements Serializable {

    @NotBlank(message = "Name is required")
    private String name;
}
