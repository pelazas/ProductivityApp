package com.example.productivityapp.presentacion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;


import java.util.List;

public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder> {

    private List<String> emails;
    private String uuid;
    private Context context;

    public UserItemAdapter(Context context, List<String> emails, String uuid) {
        this.emails = emails;
        this.uuid = uuid;
        this.context = context;
    }

    public static class UserItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserEmail;
        private Button btnAccept;
        private Button btnReject;

        public UserItemViewHolder(View itemView) {
            super(itemView);
            tvUserEmail = itemView.findViewById(R.id.textViewUserId);
            btnAccept = itemView.findViewById(R.id.buttonAceptar);
            btnReject = itemView.findViewById(R.id.buttonRechazar);
        }
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_request, parent, false);
        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
        String email = emails.get(position);

        holder.tvUserEmail.setText(email);

        holder.btnAccept.setOnClickListener(view -> {
            AppDatabase.getDatabase().getUserDAO().acceptFriendRequest(uuid, email);
            Toast.makeText(context, String.format("Ahora tú y %s sois amigos", email), Toast.LENGTH_SHORT).show();
            deleteItem(position);
        });

        holder.btnReject.setOnClickListener(view -> {
            AppDatabase.getDatabase().getUserDAO().rejectFriendRequest(uuid, email);
            Toast.makeText(context, String.format("La petición de %s ha sido rechazada", email), Toast.LENGTH_SHORT).show();
            deleteItem(position);
        });
    }

    private void deleteItem(int position) {
        emails.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, emails.size());
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }
}