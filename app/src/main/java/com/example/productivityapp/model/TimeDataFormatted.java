package com.example.productivityapp.model;


public class TimeDataFormatted {
    private String email;
    private String seconds;

    public TimeDataFormatted(String email, String seconds) {
        this.email = email;
        this.seconds = seconds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }
}
