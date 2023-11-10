package com.example.productivityapp.utils;

import android.content.Context;

import com.example.productivityapp.model.Todo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TodosLoader {

    public static List<Todo> loadTodos(Context context, String fileName) {
        BufferedReader reader = null;
        List<Todo> todos = new ArrayList<>();

        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));

            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                String title = tokens[0];
                LocalDate limitDate = LocalDate.parse(tokens[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                Todo.Priority priority = Todo.Priority.valueOf(tokens[2].toUpperCase());

                todos.add(new Todo(title, limitDate, priority));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return todos;
    }
}
