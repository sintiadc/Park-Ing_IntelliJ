package com.github.parkeer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParkeerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkeerApplication.class, args);
	}

}
