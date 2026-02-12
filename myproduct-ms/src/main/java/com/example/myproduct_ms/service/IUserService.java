package com.example.myproduct_ms.service;

import com.example.myproduct_ms.dto.req.DecreaseCountReqDto;
import com.example.myproduct_ms.dto.res.ProductResponseDto;

import java.util.List;

public interface IUserService {
    List<ProductResponseDto> getAllProducts();

    String getProductCounts(String productId);

    void decreaseProductCounts(DecreaseCountReqDto decreaseCountReqDto);
}
