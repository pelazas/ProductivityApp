package com.example.productivityapp.presentacion.social;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.model.ToDo;
import com.example.productivityapp.presentacion.LoginActivity;
import com.example.productivityapp.presentacion.MainActivity;
import com.example.productivityapp.presentacion.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class SocialFragment extends Fragment {
    private AppDatabase appDatabase;
    private TextView txAcabadas;
    private TextView txNoAcabadas;
    private FirebaseAuth mAuth;
    private TextView txtUsername;
    private Button logoutButton;
    private FirebaseUser user;

    public SocialFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_social, container, false);

        appDatabase = AppDatabase.getDatabase(requireContext());
        txAcabadas = root.findViewById(R.id.txNumTareasAcabadas);
        txNoAcabadas = root.findViewById(R.id.txNumTareasNoAcabadas);
        txtUsername = root.findViewById(R.id.txUserName);
        logoutButton = root.findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        txtUsername.setText(user.getEmail());

        logoutButton.setOnClickListener(v -> cerrarSesion());

        cargarValores();

        return root;
    }

    private void cerrarSesion() {
        mAuth.signOut();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle());
        getActivity().finish();
    }

    private void cargarValores(){
        List<ToDo> tareasAcabadas = appDatabase.getTaskDAO().getAllActive(user.getUid(), "TO_DO");
        txAcabadas.setText(tareasAcabadas.size()+"");
        List<ToDo> tareasNoAcabadas = appDatabase.getTaskDAO().getAllFinished(user.getUid());
        txNoAcabadas.setText(tareasNoAcabadas.size()+"");
    }
}