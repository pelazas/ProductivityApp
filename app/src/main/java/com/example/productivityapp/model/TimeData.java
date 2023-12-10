package com.example.productivityapp.model;

public class TimeData {
    private String email;
    private long seconds;

    public TimeData() {
        // Default constructor required for Firestore
    }

    public TimeData(String email, long seconds) {
        this.email = email;
        this.seconds = seconds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }
}
