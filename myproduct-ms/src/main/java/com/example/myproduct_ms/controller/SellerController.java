package com.example.myproduct_ms.controller;

import com.example.myproduct_ms.dto.req.CreateProductReqDto;
import com.example.myproduct_ms.dto.req.UpdateProductReqDto;
import com.example.myproduct_ms.service.ISellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/seller")
@RestController
@RequiredArgsConstructor
public class SellerController {

    private final ISellerService sellerService;

    @PostMapping("/adds-products")
    public void addProduct(@RequestBody @Valid CreateProductReqDto createProductReqDto) {
        sellerService.addProducts(createProductReqDto);
    }

    @PutMapping("/update-products/{id}")
    public void updateProduct(@PathVariable String id, @RequestBody UpdateProductReqDto updateProductReqDto) {
        sellerService.updateProducts(id, updateProductReqDto);
    }

    @DeleteMapping("/delete-products/{id}")
    public void deleteProduct(@PathVariable String id) {
        sellerService.deleteProducts(id);
    }
}
