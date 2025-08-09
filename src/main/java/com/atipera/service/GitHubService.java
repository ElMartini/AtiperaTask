package com.atipera.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GitHubService {

    public static void showReposAndBranches(String user) throws URISyntaxException, IOException, InterruptedException {

        String userURI = "https://api.github.com/users/" + user + "/repos";

        JSONArray jsonArray = createHttpRequest(userURI);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject repository = jsonArray.getJSONObject(i);
            JSONObject owner = repository.getJSONObject("owner");
            if (!checkIfFork(repository)) {
                System.out.printf("Repository: %-20s Owner: %s%n", repository.getString("name"), owner.getString("login"));
                showBranches(user, repository.getString("name"));
            }
        }


    }

    private static boolean checkIfFork(JSONObject repository) {
        return repository.getBoolean("fork");
    }

    private static void showBranches(String user, String repositoryName) throws URISyntaxException, IOException, InterruptedException {

        String branchesURI = "https://api.github.com/repos/" + user + "/" + repositoryName + "/branches";

        JSONArray jsonArray = createHttpRequest(branchesURI);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject branch = jsonArray.getJSONObject(i);
            JSONObject commit = branch.getJSONObject("commit");
            System.out.printf("Branch: %-20s SHA: %s%n", branch.getString("name"), commit.getString("sha"));
        }

    }

    private static JSONArray createHttpRequest(String branchesURI) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getBranchesRequest = HttpRequest.newBuilder()
                .uri(new URI(branchesURI))
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = httpClient.send(getBranchesRequest, HttpResponse.BodyHandlers.ofString());

        String responseBody = getResponse.body();
        return new JSONArray(responseBody);
    }
}
