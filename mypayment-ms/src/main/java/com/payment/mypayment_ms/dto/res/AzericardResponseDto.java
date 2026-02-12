package com.payment.mypayment_ms.dto.res;

import lombok.Builder;

@Builder
public record AzericardResponseDto(
        String orderId,
        String status
) {
}
