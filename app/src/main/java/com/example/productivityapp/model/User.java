package com.example.productivityapp.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userId;
    private String email;
    private List<String> friends;
    private List<String> friendRequests;

    public User(String userId, String email, List<String> friends, List<String> friendRequests) {
        this.userId = userId;
        this.email = email;
        this.friends = new ArrayList<>(friends);
        this.friendRequests = new ArrayList<>(friendRequests);
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getFriends() {
        return new ArrayList<>(friends);
    }

    public List<String> getFriendRequests() {
        return new ArrayList<>(friendRequests);
    }
}
