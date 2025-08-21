package com.atipera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.atipera")
public class AtiperaApplication {

	public static void main(String[] args){
		SpringApplication.run(AtiperaApplication.class, args);
	}

}
