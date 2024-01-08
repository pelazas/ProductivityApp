package com.example.productivityapp.model;

public interface UserDAO {
    void registerUser(String uuid, String email);
    void sendFriendRequest(String fromEmail, String toEmail);
    void acceptFriendRequest(String uuid, String friendAcceptedEmail);
    void rejectFriendRequest(String uuid, String friendRejectedEmail);
    User getUser(String uuid);
    // get user by email
    User getUserByEmail(String email);
}
