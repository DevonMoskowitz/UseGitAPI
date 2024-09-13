package com.csc340.usegitapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
public class GithubController {

    private String gitURL = "https://api.github.com/";
    private List<User> users = new ArrayList<>();

    private User checkUser(String user) {
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(user)) {
                return u;
            }
        }
        return null;
    }

    // Gets the current users in the active session
    @GetMapping("/users")
    public List<User> getCurrentUsers() {
        return users;
    }

    // Sends a hello message
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    // Use http://localhost:8080/repos?user=DevonMoskowitz as an example
    /*
    I check the github api for all the repos according to the user parameter, then get them and return a String List.
     */
    @GetMapping("/repos")
    public List<String> getRepoList(@RequestParam String user) {
        User check = checkUser(user);
        if (check != null) {
            return check.getRepos(); // Check to see if in database
        }
        List<String> repoNames = new ArrayList<>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();
            User git = new User(user);
            String jsonListResponse = restTemplate.getForObject(gitURL + git.getURL() + "/repos", String.class);
            JsonNode root = mapper.readTree(jsonListResponse);


            for (JsonNode rt : root) {
                String name = rt.get("name").asText();
                String description = rt.has("description") ? rt.get("description").asText() : "No description";
                repoNames.add(name + ": " + description);
            }
            git.setRepos(repoNames);
            users.add(git);
            return repoNames;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(GithubController.class.getName()).log(Level.SEVERE,
                    null, ex);
            return null;
        }
    }



}
