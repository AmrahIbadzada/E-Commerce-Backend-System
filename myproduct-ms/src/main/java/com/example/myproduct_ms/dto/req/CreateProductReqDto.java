package com.example.myproduct_ms.dto.req;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductReqDto(
        @NotBlank(message = "productName is null") String productName,
        @NotBlank(message = "description is null") String description,
        @Min(value = 1, message = "minimum value 1") Integer stock,
        @NotNull(message = "price is null") BigDecimal price,
        String categoryId

        ) {
}
