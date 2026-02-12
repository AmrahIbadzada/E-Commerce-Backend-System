package com.example.myproduct_ms.service.impl;

import com.example.myproduct_ms.dto.req.CreateProductReqDto;
import com.example.myproduct_ms.dto.req.UpdateProductReqDto;
import com.example.myproduct_ms.entity.ProductEntity;
import com.example.myproduct_ms.exception.ProductNotFound;
import com.example.myproduct_ms.exception.SellerNotOwnerOfProductException;
import com.example.myproduct_ms.mapper.ISellerMapper;
import com.example.myproduct_ms.repository.IProductRepository;
import com.example.myproduct_ms.service.ISellerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements ISellerService {

    private final IProductRepository productRepository;
    private final ISellerMapper sellerMapper;

    @Override
    public void addProducts(CreateProductReqDto createProductReqDto) {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        var mapper = sellerMapper
                .mapProductDtoToProductEntity(createProductReqDto, userName);
        productRepository.save(mapper);
    }

    @Override
    public void updateProducts(String id, UpdateProductReqDto updateProductReqDto) {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        ProductEntity productEntity =
                productRepository.findByProductId(id).orElseThrow(() -> new ProductNotFound("Product not found with id"));

        if (productEntity.getUserName().equals(userName)) {

            sellerMapper.mapUpdateProductDtoToProductEntity(updateProductReqDto, productEntity);

            productRepository.save(productEntity);
        } else {
            throw new SellerNotOwnerOfProductException("Seller not found");
        }
    }

    @Override
    @Transactional
    public void deleteProducts(String id) {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        ProductEntity productEntity =
                productRepository.findByProductId(id).orElseThrow(() -> new ProductNotFound("Product not found with id"));

        if (productEntity.getUserName().equals(auth.getName())) {
            productRepository.delete(productEntity);
        } else {
            throw new SellerNotOwnerOfProductException("Seller cannot delete product");
        }
    }
}
