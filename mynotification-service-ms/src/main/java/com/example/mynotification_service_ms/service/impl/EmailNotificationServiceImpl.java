package com.example.mynotification_service_ms.service.impl;

import com.example.mynotification_service_ms.dto.KafkaReqDto;
import com.example.mynotification_service_ms.service.IEmailNotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailNotificationServiceImpl implements IEmailNotificationService {

    JavaMailSender javaMailSender;

    @Override
    public void sendEmail(KafkaReqDto mailSendReqDto) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailSendReqDto.email());
        mailMessage.setSubject("Payment completed successfully");
        mailMessage.setText(
                "Salam " + mailSendReqDto.userName() + ",\n\n" +
                        "Your order has been received.\n" +
                        "Order ID: " + mailSendReqDto.orderId()
        );
        javaMailSender.send(mailMessage);
    }
}
