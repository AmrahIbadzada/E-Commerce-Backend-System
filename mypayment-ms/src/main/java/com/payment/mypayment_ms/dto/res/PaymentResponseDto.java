package com.payment.mypayment_ms.dto.res;

import lombok.Builder;
import lombok.Data;

@Builder
public record PaymentResponseDto(
        String orderId,
        String status
) {
}
