package com.atipera;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import com.atipera.service.GithubService;
import com.atipera.controller.GithubController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class AtiperaApplicationTests {


    @Autowired
    private GithubService githubService;


    @Test
    void githubRespositoriesTest() throws URISyntaxException, IOException, InterruptedException, JSONException {

        String user = "ElMartini";
//        String user = "asczkhnalpisnckxmzoispwca";
        HttpResponse<String> response = githubService.createHttpRequest("https://api.github.com/users/" + user + "/repos");
        boolean isEmpty = true;
        boolean responseCode = (response.statusCode() == 200);
        if (responseCode) {
            JSONArray body = new JSONArray(response.body());
            isEmpty = body.length()==0;
        }
        System.setIn(new ByteArrayInputStream(user.getBytes()));

        GithubController controller = new GithubController(githubService);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        System.setOut(new PrintStream(outputStream));

        controller.runController();

        System.setOut(originalOutput);
        String output = outputStream.toString();
        if (responseCode) {
            if (isEmpty) {
                assertTrue(output.contains("User has none public or not fork repositories"), "Output should contains 'User has none public or not fork repositories'");

            } else {
                assertTrue(output.contains("Repository"), "Output should contains 'Repository'");
                assertTrue(output.contains("Owner"), "Output should contains 'Owner'");

                assertTrue(output.contains("Branch"), "Output should contains 'Branch'");
                assertTrue(output.contains("SHA"), "Output should contains 'SHA'");
            }
        } else {
            assertTrue(output.contains("status"), "Output should contains 'status: '");
            assertTrue(output.contains("message"), "Output should contains 'message: '");
        }


    }

}
