package com.example.productivityapp.model;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class Converters {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static ToDo.Priority fromPriorityString(String value) {
        return value == null ? null : ToDo.Priority.valueOf(value);
    }

    public static String priorityToString(ToDo.Priority priority) {
        return priority == null ? null : priority.name();
    }

    public static ToDo.State fromStateString(String value) {
        return value == null ? null : ToDo.State.valueOf(value);
    }

    public static String stateToString(ToDo.State state) {
        return state == null ? null : state.name();
    }

    public static LocalDate fromLocalDateString(String value) {
        return value == null ? null : LocalDate.parse(value, formatter);
    }

    public static String localDateToString(LocalDate localDate) {
        return localDate == null ? null : localDate.format(formatter);
    }
}
