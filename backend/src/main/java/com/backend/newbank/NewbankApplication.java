package com.backend.newbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewbankApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewbankApplication.class, args);
	}

}
