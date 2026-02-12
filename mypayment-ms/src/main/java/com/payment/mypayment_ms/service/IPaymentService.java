package com.payment.mypayment_ms.service;

import com.payment.mypayment_ms.dto.req.PaymentRequestDto;
import com.payment.mypayment_ms.dto.res.PaymentResponseDto;

public interface IPaymentService {

    PaymentResponseDto payProduct(PaymentRequestDto paymentRequestDto);
}
