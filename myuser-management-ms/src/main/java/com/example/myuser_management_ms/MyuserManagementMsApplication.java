package com.example.myuser_management_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MyuserManagementMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyuserManagementMsApplication.class, args);
	}

}
