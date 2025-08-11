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


    public void showReposAndBranches(String user) throws URISyntaxException, IOException, InterruptedException {

        String userURI = "https://api.github.com/users/" + user + "/repos";

        HttpResponse<String> response = createHttpRequest(userURI);
        if (correctResponseCode(response)) {
            showRepositories(new JSONArray(response.body()), user);
        }
    }

    private void showRepositories(JSONArray jsonResponseBody, String user) throws URISyntaxException, IOException, InterruptedException {

        if (jsonResponseBody.length()==0) {
            System.out.println("User has none public repositories");
        } else {
            for (int i = 0; i < jsonResponseBody.length(); i++) {
                JSONObject repository = jsonResponseBody.getJSONObject(i);
                JSONObject owner = repository.getJSONObject("owner");
                if (!checkIfFork(repository)) {
                    System.out.printf("Repository: %-20s Owner: %s%n", repository.getString("name"), owner.getString("login"));

                    showBranches(user, repository.getString("name"));
                } else {
                    System.out.println("Repository: None Owner: None");
                    System.out.println("Branch: None SHA: None");
                    System.out.println("This user has no public repository that isn't fork");
                }
            }
        }
    }

    private boolean checkIfFork(JSONObject repository) {
        return repository.getBoolean("fork");
    }

    private void showBranches(String user, String repositoryName) throws URISyntaxException, IOException, InterruptedException {

        String branchesURI = "https://api.github.com/repos/" + user + "/" + repositoryName + "/branches";

        String response = createHttpRequest(branchesURI).body();
        JSONArray jsonArray = new JSONArray(response);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject branch = jsonArray.getJSONObject(i);
            JSONObject commit = branch.getJSONObject("commit");
            System.out.printf("Branch: %-20s SHA: %s%n", branch.getString("name"), commit.getString("sha"));
        }

    }

    public HttpResponse<String> createHttpRequest(String URI) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(URI))
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private boolean correctResponseCode(HttpResponse<String> response) {

        if (response.statusCode() == 200) {
            return true;
        } else {
            String json = String.format(
                    "{\n\"status\": %d,\n\"message\": \"%s\"\n}",
                    response.statusCode(),
                    new JSONObject(response.body()).getString("message")
            );
            System.out.println(json);
        }
        return false;
    }


}
