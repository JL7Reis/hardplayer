package com.softplayer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.softplayer.domain.ReturnObject;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		new SpringApplication(Application.class).run(args);
	}
	
	@Bean
	public ReturnObject returnObject() {
		return new ReturnObject();
	}
}