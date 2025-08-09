package com.atipera;

import com.atipera.controller.GitHubController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
public class AtiperaApplication {

	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		SpringApplication.run(AtiperaApplication.class, args);
		GitHubController.runController();
	}

}
