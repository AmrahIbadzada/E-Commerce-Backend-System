package com.payment.mypayment_ms.feign;

import com.payment.mypayment_ms.dto.SuccessDto;
import com.payment.mypayment_ms.dto.req.DecreaseCountReqDto;
import com.payment.mypayment_ms.dto.res.CountResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        url = "${feign.client.product-service.url}"
)

public interface IProductFeignClient {

    @GetMapping(value = "/users/counts/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    String getCountsProducts(@PathVariable String productId);

    @PutMapping(value = "/users/decrease-count", produces = MediaType.APPLICATION_JSON_VALUE)
    void decreaseCount(@RequestBody DecreaseCountReqDto decreaseCountReqDto);
}
