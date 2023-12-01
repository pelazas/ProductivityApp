package com.example.productivityapp.presentacion.toDo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivityapp.presentacion.adapters.ListToDosAdapter;
import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.model.ToDo;

import java.util.List;

public class ToDoFragment extends Fragment {

    private RecyclerView rv;
    private List<ToDo> todos;
    public static final String TODO_SELECTED = "todo_selected";

    public ToDoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_do, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadTodos();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.todos_recyclerview);

        loadTodos();

        Button btn = view.findViewById(R.id.add_btn);
        btn.setOnClickListener(v -> addTodo());
    }

    private void addTodo() {
        Intent intent = new Intent(requireContext(), AddToDoActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle());
    }

    private void clickOnItem(ToDo todo){
        Intent intent = new Intent(requireContext(), AddToDoActivity.class);
        intent.putExtra(TODO_SELECTED, todo);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle());
    }

    private void loadTodos() {
        todos = AppDatabase.getDatabase(requireContext()).getTaskDAO().getAllActive("TO_DO");
        ListToDosAdapter lta = new ListToDosAdapter(requireContext(), todos, this::clickOnItem);
        rv.setAdapter(lta);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}