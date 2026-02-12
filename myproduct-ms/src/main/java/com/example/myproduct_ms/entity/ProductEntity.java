package com.example.myproduct_ms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @Column(name = "product_id", nullable = false, length = 255)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;

    @Column(name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false)
    private LocalDateTime updatedAt;
}
