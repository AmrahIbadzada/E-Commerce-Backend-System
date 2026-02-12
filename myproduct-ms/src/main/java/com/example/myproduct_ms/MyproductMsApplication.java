package com.example.myproduct_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MyproductMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyproductMsApplication.class, args);
	}

}
