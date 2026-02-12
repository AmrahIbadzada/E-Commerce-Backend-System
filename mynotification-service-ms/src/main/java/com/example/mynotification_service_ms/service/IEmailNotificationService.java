package com.example.mynotification_service_ms.service;

import com.example.mynotification_service_ms.dto.KafkaReqDto;

public interface IEmailNotificationService {

//    void sendEmail(String email, String subject, String body);
    void sendEmail(KafkaReqDto mailSendReqDto);
}
