package com.example.myproduct_ms.mapper;

import com.example.myproduct_ms.dto.res.ProductResponseDto;
import com.example.myproduct_ms.entity.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    List<ProductResponseDto> mapListEntityToListProductEntity(List<ProductEntity> productEntities);
}
