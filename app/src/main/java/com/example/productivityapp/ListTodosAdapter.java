package com.example.productivityapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivityapp.model.Todo;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListTodosAdapter extends RecyclerView.Adapter<ListTodosAdapter.TodoViewHolder> {

    private Context context;
    private List<Todo> todos;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

    public ListTodosAdapter(Context context, List<Todo> todos) {
        this.context = context;
        this.todos = todos;
    }

    @NonNull
    @Override
    public ListTodosAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.recycler_view_todo, parent, false);

        return new ListTodosAdapter.TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTodosAdapter.TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);

        holder.titleText.setText(todo.getTitle());
        holder.limitDateText.setText(todo.getLimitDate().format(formatter));
        holder.priorityText.setText(todo.getPriority().name());
        holder.priorityText.setBackgroundResource(getBackgroundForPriority(todo.getPriority()));
    }

    private int getBackgroundForPriority(Todo.Priority priority) {
        switch (priority) {
            case LOW:
                return R.drawable.rounded_green_bg;
            case MEDIUM:
                return R.drawable.rounded_yellow_bg;
            case HIGH:
                return R.drawable.rounded_red_bg;
            default:
                throw new RuntimeException("Unknown priority");
        }
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {

        private TextView titleText;
        private TextView limitDateText;
        private TextView priorityText;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.todo_title_text);
            limitDateText = itemView.findViewById(R.id.limit_date_text);
            priorityText = itemView.findViewById(R.id.priority_text);
        }
    }
}
