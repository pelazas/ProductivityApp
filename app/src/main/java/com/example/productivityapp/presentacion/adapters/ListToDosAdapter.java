package com.example.productivityapp.presentacion.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.PrecomputedText;
import android.view.LayoutInflater;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.model.ToDo;
import com.google.android.material.snackbar.Snackbar;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListToDosAdapter extends RecyclerView.Adapter<ListToDosAdapter.TodoViewHolder> {
    private final static int MAX_CHARACTERS_TITLE = 12;
    private Context context;
    private List<ToDo> todos;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
    private OnItemClickListener listener;

    public ListToDosAdapter(Context context, List<ToDo> todos, OnItemClickListener onItemClickListener) {
        cargarColeccion(todos);
        this.context = context;
        this.listener = onItemClickListener;
    }

    private void cargarColeccion(List<ToDo> todos){
        this.todos = new ArrayList<ToDo>();
        for(int i = todos.size() - 1; i >= 0; i--){
            this.todos.add(todos.get(i));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ToDo todo);
    }

    @NonNull
    @Override
    public ListToDosAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.recycler_view_todo, parent, false);

        CheckBox item = view.findViewById(R.id.todo_title_text);
        item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpannableString spannableString = new SpannableString(item.getText());
                    spannableString.setSpan(new StrikethroughSpan(), 0, item.getText().length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    item.setText(spannableString);
                }else{
                    item.setText(item.getText().toString());
                }
            }
        });
        return new ListToDosAdapter.TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListToDosAdapter.TodoViewHolder holder, int position) {
        ToDo todo = todos.get(position);

        holder.titleText.setText(shortenString(todo.getTitle()));
        holder.limitDateText.setText(todo.getLimitDate().format(formatter));
        holder.priorityText.setText(todo.getPriority().name());
        holder.priorityText.setBackgroundResource(getBackgroundForPriority(todo.getPriority()));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(todo));
    }

    public static String shortenString(String input) {
        if (input.length() > MAX_CHARACTERS_TITLE) {
            return input.substring(0, MAX_CHARACTERS_TITLE) + "...";
        } else {
            return input;
        }
    }

    private int getBackgroundForPriority(ToDo.Priority priority) {
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
