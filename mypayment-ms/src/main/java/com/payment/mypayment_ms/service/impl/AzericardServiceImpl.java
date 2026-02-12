package com.payment.mypayment_ms.service.impl;

import com.payment.mypayment_ms.dto.req.PaymentRequestDto;
import com.payment.mypayment_ms.dto.res.AzericardResponseDto;
import com.payment.mypayment_ms.service.IAzericardService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AzericardServiceImpl implements IAzericardService {

    private static final Random RANDOM = new Random();

    @Override
    public AzericardResponseDto payAzericard(PaymentRequestDto paymentRequestDto) {

        int orderId = 100000 + RANDOM.nextInt(900000);
        return AzericardResponseDto.builder()
                .orderId(String.valueOf(orderId))
                .status("1")
                .build();
    }
}
