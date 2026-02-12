package com.example.myuser_management_ms.entity;

import com.example.myuser_management_ms.utils.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;


@Table(name = "users")
@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", length =  30, nullable = false)
    private String name;

    private String surname;

    @Column(name = "user_name", length =  22, nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "email", length = 65, nullable = false)
    private String email;

    private String phoneNumber;


    private Instant birthDate;

    @CurrentTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
