package com.example.productivityapp.presentacion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivityapp.R; // Make sure to replace this with the actual package name
import com.example.productivityapp.model.User;

import java.util.List;

public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder> {

    private List<User> userList;

    public UserItemAdapter(List<User> userList) {
        this.userList = userList;
    }

    public static class UserItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUser;
        TextView textViewUserId;
        Button buttonAceptar;

        public UserItemViewHolder(View itemView) {
            super(itemView);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            textViewUserId = itemView.findViewById(R.id.textViewUserId);
            buttonAceptar = itemView.findViewById(R.id.buttonAceptar);
        }
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_friends_search, parent, false);
        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
        User user = userList.get(position);

        // Set data to views
        holder.textViewUser.setText("Usuario: " + user.getEmail()); // Replace with the actual method to get user name
        holder.textViewUserId.setText(user.getUserId()); // Replace with the actual method to get user ID

        // Set click listener for the "Aceptar" button
        holder.buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the button click event as needed
                // You can access the user associated with this item using userList.get(position)
                // For example, userList.get(position).getUserId()
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}