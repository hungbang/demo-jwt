package com.aptech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:sms.properties")
public class DemoJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoJwtApplication.class, args);
	}
}
