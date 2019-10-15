package com.softplayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.softplayer.domain.ReturnObject;

@SpringBootApplication
public class SoftplayerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoftplayerApplication.class, args);
	}
	
	@Bean
	public ReturnObject returnObject() {
		return new ReturnObject();
	}
}