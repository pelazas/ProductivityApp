package com.example.productivityapp.presentacion.social;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.model.ToDo;
import com.example.productivityapp.presentacion.MainActivity;

import java.util.List;

public class SocialFragment extends Fragment {
    private AppDatabase appDatabase;
    private TextView txAcabadas;
    private TextView txNoAcabadas;

    public SocialFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_social, container, false);

        appDatabase = AppDatabase.getDatabase(requireContext());
        txAcabadas = root.findViewById(R.id.txNumTareasAcabadas);
        txNoAcabadas = root.findViewById(R.id.txNumTareasNoAcabadas);
        cargarValores();

        return root;
    }

    private void cargarValores(){
        List<ToDo> tareasAcabadas = appDatabase.getTaskDAO().getAllActive("TO_DO");
        txAcabadas.setText(tareasAcabadas.size()+"");
        List<ToDo> tareasNoAcabadas = appDatabase.getTaskDAO().getAllFinished();
        txNoAcabadas.setText(tareasNoAcabadas.size()+"");
    }
}