package com.atipera.controller;

import com.atipera.service.GitHubService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


@Component
public class GitHubController {

    @EventListener(ApplicationReadyEvent.class)
    public void runController() throws URISyntaxException, IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter user login: ");
        String user =scanner.nextLine();
        String encodedUser = URLEncoder.encode(user, StandardCharsets.UTF_8);
        GitHubService gitHubService = new GitHubService();
        gitHubService.showReposAndBranches(encodedUser);
    }
}
