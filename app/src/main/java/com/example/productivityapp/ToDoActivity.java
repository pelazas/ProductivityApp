package com.example.productivityapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.productivityapp.model.Todo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ToDoActivity extends AppCompatActivity {

    private List<Todo> todos;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        RecyclerView rv = findViewById(R.id.todos_recyclerview);

        loadTodos();

        ListTodosAdapter lta = new ListTodosAdapter(this, todos);
        rv.setAdapter(lta);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadTodos() {
        BufferedReader reader = null;
        todos = new ArrayList<>();

        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("todos.csv")));

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
    }
}