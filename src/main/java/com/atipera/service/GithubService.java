package com.atipera.service;

import com.atipera.dto.BranchDTO;
import com.atipera.dto.RepositoryDTO;
import com.atipera.dto.ResponseDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class GithubService {

    @Value("${github.api.base-url}")
    private String githubBaseUrl;

    @Value("${github.api.base-branch-url}")
    private String githubBaseBranchUrl;

    public ResponseDTO getUserData(String user) throws URISyntaxException, IOException, InterruptedException {

        String userURI = githubBaseUrl + user + "/repos";

        HttpResponse<String> response = createHttpRequest(userURI);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setStatusCode(response.statusCode());
        if (response.statusCode() == 200) {
            responseDTO.setRepositoryDTOList(processResponse(new JSONArray(response.body()), user));
        } else {
            JSONObject errorJson = new JSONObject(response.body());
            responseDTO.setMessage(errorJson.optString("message", "Unexpected error"));
        }
        return responseDTO;
    }

    private List<RepositoryDTO> processResponse(JSONArray jsonResponseBody, String user) throws URISyntaxException, IOException, InterruptedException {

        List<RepositoryDTO> repositoryDTOList = new ArrayList<>();
        if (jsonResponseBody.isEmpty()) {
            return repositoryDTOList;
        } else {

            for (int i = 0; i < jsonResponseBody.length(); i++) {
                JSONObject repository = jsonResponseBody.getJSONObject(i);
                JSONObject owner = repository.getJSONObject("owner");
                if (!checkIfFork(repository)) {
                    RepositoryDTO repositoryDTO = new RepositoryDTO();
                    repositoryDTO.setName(repository.getString("name"));
                    repositoryDTO.setOwner(owner.getString("login"));
                    repositoryDTO.setBranches(getBranches(user, repository.getString("name")));
                    repositoryDTOList.add(repositoryDTO);
                }
            }
        }
        return repositoryDTOList;
    }

    private boolean checkIfFork(JSONObject repository) {
        return repository.getBoolean("fork");
    }

    private List<BranchDTO> getBranches(String user, String repositoryName) throws URISyntaxException, IOException, InterruptedException {


        String branchesURI = githubBaseBranchUrl + user + "/" + repositoryName + "/branches";
        String response = createHttpRequest(branchesURI).body();
        JSONArray jsonArray = new JSONArray(response);

        List<BranchDTO> branchDTOList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            BranchDTO branchDTO = new BranchDTO();
            JSONObject branch = jsonArray.getJSONObject(i);
            JSONObject commit = branch.getJSONObject("commit");
            branchDTO.setName(branch.getString("name"));
            branchDTO.setSHA(commit.getString("sha"));
            branchDTOList.add(branchDTO);
        }
        return branchDTOList;
    }

    public HttpResponse<String> createHttpRequest(String URI) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(URI))
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
