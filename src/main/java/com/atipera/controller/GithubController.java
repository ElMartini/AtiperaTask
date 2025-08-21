package com.atipera.controller;

import com.atipera.dto.BranchDTO;
import com.atipera.dto.RepositoryDTO;
import com.atipera.dto.ResponseDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import com.atipera.service.GithubService;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;


@Controller
@Profile("!test")
public class GithubController implements CommandLineRunner {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    public void runController()  throws URISyntaxException, IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter user login: ");
        String user = scanner.nextLine();
        String encodedUser = URLEncoder.encode(user, StandardCharsets.UTF_8);

        ResponseDTO response = githubService.getUserData(encodedUser);

        if (response.getStatusCode() == 200) {
            if (response.getRepositoryDTOList().isEmpty()) {
                noPublicRepositoriesResponse();
            } else {
                for (RepositoryDTO repository : response.getRepositoryDTOList()) {
                    System.out.printf("Repository: %-20s Owner: %s%n", repository.getName(), repository.getOwner());
                    printBranches(repository.getBranches());
                }
            }
        } else {
            statusCodeNotOK(response);
        }
    }

    public void noPublicRepositoriesResponse() {
        System.out.println("User has none public or not fork repositories");

    }

    public void statusCodeNotOK(ResponseDTO response) {

        System.out.println(
                "{\n\"status\": " + response.getStatusCode()
                        + "\n\"message\": " + response.getMessage()
                        + "\n}"
        );
    }

    public void printBranches(List<BranchDTO> branches) {
        for (BranchDTO branch : branches) {
            System.out.printf("Branch: %-30s SHA: %s%n", branch.getName(), branch.getSHA());

        }
    }

    @Override
    public void run(String... args) throws Exception {
        runController();
    }
}
