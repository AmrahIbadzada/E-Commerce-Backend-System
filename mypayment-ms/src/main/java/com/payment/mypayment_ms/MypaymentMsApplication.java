package com.payment.mypayment_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MypaymentMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MypaymentMsApplication.class, args);
	}

}
