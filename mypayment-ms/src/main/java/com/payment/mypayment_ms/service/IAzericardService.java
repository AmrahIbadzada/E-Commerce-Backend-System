package com.payment.mypayment_ms.service;

import com.payment.mypayment_ms.dto.req.PaymentRequestDto;
import com.payment.mypayment_ms.dto.res.AzericardResponseDto;

public interface IAzericardService {

    AzericardResponseDto payAzericard(PaymentRequestDto paymentRequestDto);
}
