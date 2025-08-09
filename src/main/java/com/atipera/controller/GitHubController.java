package com.atipera.controller;

import com.atipera.service.GitHubService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class GitHubController {

    public static void runController() throws URISyntaxException, IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter user login: ");
        String user =scanner.nextLine();
        GitHubService.showReposAndBranches(user);
    }
}
