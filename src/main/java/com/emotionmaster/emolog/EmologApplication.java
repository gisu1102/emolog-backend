package com.emotionmaster.emolog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EmologApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmologApplication.class, args);
	}

}
