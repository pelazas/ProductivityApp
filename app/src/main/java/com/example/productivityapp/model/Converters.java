package com.example.productivityapp.model;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Converters {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @TypeConverter
    public static Todo.Priority fromPriorityString(String value) {
        return value == null ? null : Todo.Priority.valueOf(value);
    }

    @TypeConverter
    public static String priorityToString(Todo.Priority priority) {
        return priority == null ? null : priority.name();
    }

    @TypeConverter
    public static LocalDate fromLocalDateString(String value) {
        return value == null ? null : LocalDate.parse(value, formatter);
    }

    @TypeConverter
    public static String localDateToString(LocalDate localDate) {
        return localDate == null ? null : localDate.format(formatter);
    }
}
