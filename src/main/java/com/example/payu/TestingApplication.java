package com.example.payu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class TestingApplication {

	private static Class applicationClass = TestingApplication.class;

	public static void main(String[] args) {
		SpringApplication.run(TestingApplication.class, args);
	}

	/*
	 * protected SpringApplicationBuilder configure(SpringApplicationBuilder
	 * application) { return application.sources(applicationClass); }
	 */

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
