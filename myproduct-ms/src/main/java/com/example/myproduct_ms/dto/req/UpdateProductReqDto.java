package com.example.myproduct_ms.dto.req;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UpdateProductReqDto(String productName,
                                  String description,
                                  Integer stock,
                                  BigDecimal price,
                                  String categoryId) {
}
