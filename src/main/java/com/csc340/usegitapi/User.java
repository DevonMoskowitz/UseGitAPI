package com.csc340.usegitapi;

import java.util.List;

public class User {
    private String name;
    private List<String> repos;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return "/users/" + name;
    }

    public void setRepos(List<String> repos) {
        this.repos = repos;
    }

    public List<String> getRepos() {
        return repos;
    }
}
