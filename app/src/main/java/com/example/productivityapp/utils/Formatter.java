package com.example.productivityapp.utils;

public class Formatter {

    public static String formatTime(long minutes, long seconds) {
        return String.format("%02d:%02d", minutes, seconds);
    }
}
