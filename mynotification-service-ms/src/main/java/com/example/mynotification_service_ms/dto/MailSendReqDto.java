package com.example.mynotification_service_ms.dto;

public record MailSendReqDto(String email,
                             String subject,
                             String body) {
}
