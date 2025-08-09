package com.atipera;

import com.atipera.service.GitHubService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class AtiperaApplication {

	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
		SpringApplication.run(AtiperaApplication.class, args);
		GitHubService.run();
	}

}
