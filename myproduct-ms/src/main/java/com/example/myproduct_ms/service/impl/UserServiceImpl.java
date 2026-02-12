package com.example.myproduct_ms.service.impl;

import com.example.myproduct_ms.dto.req.DecreaseCountReqDto;
import com.example.myproduct_ms.dto.res.ProductResponseDto;
import com.example.myproduct_ms.entity.ProductEntity;
import com.example.myproduct_ms.exception.ProductNotFound;
import com.example.myproduct_ms.mapper.IUserMapper;
import com.example.myproduct_ms.repository.IProductRepository;
import com.example.myproduct_ms.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IProductRepository productRepository;
    private final IUserMapper userMapper;

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<ProductEntity> allProducts = productRepository.findAll();
        return userMapper.mapListEntityToListProductEntity(allProducts);
    }

    @Override
    public String getProductCounts(String productId) {

        ProductEntity productEntity =
                productRepository.findByProductId(productId).orElseThrow(()
                        -> new ProductNotFound("Product not found with id"));

        return productEntity.getStock().toString();

    }

    @Override
    public void decreaseProductCounts(DecreaseCountReqDto decreaseCountReqDto) {

        ProductEntity productEntity =
                productRepository.findByProductId(decreaseCountReqDto
                        .productId()).orElseThrow(()
                        -> new ProductNotFound("Product not found with id"));

        if (productEntity.getStock() < decreaseCountReqDto.count()) {
            throw new RuntimeException("Stock is not enough");
        }

        productEntity.setStock(productEntity.getStock() - decreaseCountReqDto.count());
        productRepository.save(productEntity);
    }
}
