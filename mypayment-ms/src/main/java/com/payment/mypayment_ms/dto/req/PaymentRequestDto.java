package com.payment.mypayment_ms.dto.req;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentRequestDto(String productId,
                                Integer quantity,
                                BigDecimal price) {
}
