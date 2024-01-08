package com.example.productivityapp.presentacion.social.requests;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.presentacion.adapters.UserItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RequestsFragment extends Fragment {

    private RecyclerView rv;
    private TextView tvNoRequests;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private List<String> friendRequests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        View root = inflater.inflate(R.layout.fragment_requests, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.request_recyclerview);
        tvNoRequests = view.findViewById(R.id.tvNoRequests);

        loadRequests();
    }

    private void loadRequests() {
        this.friendRequests = AppDatabase.getDatabase().getUserDAO().getUser(user.getUid()).getFriendRequests();

        if (friendRequests.size() > 0) {
            tvNoRequests.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }

        UserItemAdapter uia = new UserItemAdapter(this.getContext(), friendRequests, mAuth.getUid());
        rv.setAdapter(uia);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}