package com.davidcamelo.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		log.info("Current java.home {}", System.getProperty("java.home"));
		log.info("Current java.vendor {}", System.getProperty("java.vendor"));
		log.info("Current java.vendor.url {}", System.getProperty("java.vendor.url"));
		log.info("Current java.version {}", System.getProperty("java.version"));

		SpringApplication.run(UserApplication.class, args);
	}
}
