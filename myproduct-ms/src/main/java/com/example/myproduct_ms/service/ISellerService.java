package com.example.myproduct_ms.service;

import com.example.myproduct_ms.dto.req.CreateProductReqDto;
import com.example.myproduct_ms.dto.req.UpdateProductReqDto;

public interface ISellerService {

    void addProducts(CreateProductReqDto createProductReqDto);

    void updateProducts(String id, UpdateProductReqDto updateProductReqDto);

    void deleteProducts(String id);
}
