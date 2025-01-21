package com.hmsapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HmsappApplication {

	public static void main(String[] args) {
		SpringApplication.run(HmsappApplication.class, args);
	}
	@Bean
	public ModelMapper getMapper(){
		return new ModelMapper();
	}
}
