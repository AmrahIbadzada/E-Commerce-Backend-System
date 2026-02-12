package com.payment.mypayment_ms.controller;

import com.payment.mypayment_ms.dto.SuccessDto;
import com.payment.mypayment_ms.dto.req.PaymentRequestDto;
import com.payment.mypayment_ms.dto.res.PaymentResponseDto;
import com.payment.mypayment_ms.service.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.payment.mypayment_ms.utils.Status.SUCCESS;


@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;

    @PostMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SuccessDto<PaymentResponseDto>> payments (
            @RequestBody PaymentRequestDto paymentRequestDto ) {

        PaymentResponseDto paymentResponseDto =
                paymentService.payProduct(paymentRequestDto);

        SuccessDto<PaymentResponseDto> successDto = new SuccessDto<>(SUCCESS, paymentResponseDto);

        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }
}
