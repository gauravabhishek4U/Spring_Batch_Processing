package com.batchProcessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.exit(SpringApplication.run(SpringBatchProcessingApplication.class, args));
	}

}
