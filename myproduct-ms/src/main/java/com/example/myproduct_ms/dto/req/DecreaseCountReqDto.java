package com.example.myproduct_ms.dto.req;

import jakarta.validation.constraints.NotNull;

public record DecreaseCountReqDto(
        @NotNull(message = "productId is null!") String productId,
        @NotNull(message = "count is null!") Integer count
) {
}
