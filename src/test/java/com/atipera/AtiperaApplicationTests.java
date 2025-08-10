package com.atipera;

import com.atipera.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;


class AtiperaApplicationTests {

	@Test
	void testGithubRespositories() throws URISyntaxException, IOException, InterruptedException {
		String user = "ElMartini";
		GitHubService gitHubService = new GitHubService();
		gitHubService.showReposAndBranches(user);
	}

}
