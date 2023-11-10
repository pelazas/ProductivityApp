package com.example.productivityapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.model.Todo;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ToDoActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private RecyclerView rv;

    private List<Todo> todos;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        rv = findViewById(R.id.todos_recyclerview);
        navView = findViewById(R.id.bottom_nav_view);

        todos = AppDatabase.getDatabase(this).getTaskDAO().getAll();

        ListTodosAdapter lta = new ListTodosAdapter(this, todos);
        rv.setAdapter(lta);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTodo();
            }
        });

        navView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        todos = AppDatabase.getDatabase(this).getTaskDAO().getAll();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        ListTodosAdapter lta = new ListTodosAdapter(this, todos);
        rv.setAdapter(lta);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addTodo() {
        Intent intent = new Intent(ToDoActivity.this, AddTodoActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_timer){
            startActivity(new Intent(this, MainActivity.class));
            return true;
        } else if (itemId == R.id.nav_toDo){
            return true;
        } else if (itemId == R.id.nav_social){
            return true;
        } else {
            throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
    };
}