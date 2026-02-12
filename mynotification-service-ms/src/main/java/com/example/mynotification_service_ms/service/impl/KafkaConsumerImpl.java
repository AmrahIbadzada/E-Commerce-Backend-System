package com.example.mynotification_service_ms.service.impl;

import com.example.mynotification_service_ms.dto.KafkaReqDto;
import com.example.mynotification_service_ms.service.IEmailNotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class KafkaConsumerImpl {

    IEmailNotificationService emailNotificationService;
    KafkaTemplate<String, KafkaReqDto> kafkaTemplate;

    @KafkaListener(topics = "kafka-payment-topic",
            groupId = "notification-group")
    public void consume(KafkaReqDto kafkaReqDto,
                        Acknowledgment ack) {
        log.info("Received message: {}", kafkaReqDto);

        emailNotificationService.sendEmail(kafkaReqDto);
        log.info("Message sent successfully");
        ack.acknowledge();
    }
}
