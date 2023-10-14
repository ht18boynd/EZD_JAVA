package com.ezportal.portal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EntityScan("com.ezservice.service.models")
@ComponentScan(basePackages = {"com.ezportal.portal","com.ezservice.service"})
public class EzPortalApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(EzPortalApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
	}

}
