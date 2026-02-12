package com.example.myproduct_ms.controller;

import com.example.myproduct_ms.dto.SuccessDto;
import com.example.myproduct_ms.dto.req.DecreaseCountReqDto;
import com.example.myproduct_ms.dto.res.ProductResponseDto;
import com.example.myproduct_ms.service.IUserService;
import com.example.myproduct_ms.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SuccessDto<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> productResponseDtos = userService.getAllProducts();

        SuccessDto<List<ProductResponseDto>> successDto = new SuccessDto<>(Status.SUCCESS, productResponseDtos);

        return new ResponseEntity<>(successDto, HttpStatus.OK);
    }

    @GetMapping(value = "/users/counts/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    String getCountsProducts(@PathVariable String productId) {
        return userService.getProductCounts(productId);
    }

    @PutMapping(value = "/users/decrease-count")
    void decreaseCount(@RequestBody DecreaseCountReqDto decreaseCountReqDto) {
        userService.decreaseProductCounts(decreaseCountReqDto);
    }
}
