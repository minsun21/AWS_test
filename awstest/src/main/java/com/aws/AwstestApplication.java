package com.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AwstestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwstestApplication.class, args);
	}
}
