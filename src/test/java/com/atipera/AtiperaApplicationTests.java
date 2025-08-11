package com.atipera;

import com.atipera.controller.GitHubController;
import com.atipera.service.GitHubService;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;


class AtiperaApplicationTests {

    @Test
    void githubRespositoriesTest() throws URISyntaxException, IOException, InterruptedException, JSONException {

        String user = "ElMartini";
        GitHubService gitHubService = new GitHubService();
        HttpResponse<String> response = gitHubService.createHttpRequest("https://api.github.com/users/" + user + "/repos");
        JSONArray body = new JSONArray(response.body());
        boolean isEmpty = body.length() == 0;

        System.setIn(new ByteArrayInputStream(user.getBytes()));

        GitHubController controller = new GitHubController();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        System.setOut(new PrintStream(outputStream));

        controller.runController();

        System.setOut(originalOutput);
        String output = outputStream.toString();
        if (isEmpty) {
            assertTrue(output.contains("User has none public repositories"), "Output should contains 'User has none public repositories'");

        } else {
            assertTrue(output.contains("Repository"), "Output should contains 'Repository'");
            assertTrue(output.contains("Owner"), "Output should contains 'Owner'");

            assertTrue(output.contains("Branch"), "Output should contains 'Branch'");
            assertTrue(output.contains("SHA"), "Output should contains 'SHA'");
        }

    }

}
