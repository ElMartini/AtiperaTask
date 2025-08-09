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

        String token = System.getenv("GITHUB_TOKEN");
        String tokenHeader = "Bearer "+token;
        String userURI = "https://api.github.com/users/"+user+"/repos";

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(userURI))
                .header("Authorization", tokenHeader)
                .GET()
                .build();


        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        String responseBody = getResponse.body();
        JSONArray jsonArray = new JSONArray(responseBody);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject repository = jsonArray.getJSONObject(i);
            JSONObject owner = repository.getJSONObject("owner");
            if(!checkIfFork(repository))
                System.out.printf("Repository: %-20s Owner: %s%n", repository.getString("name"), owner.getString("login"));
        }




    }

    private static boolean checkIfFork(JSONObject repository) {
        return repository.getBoolean("fork");
    }
}
