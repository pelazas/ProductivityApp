package com.example.productivityapp.presentacion.social;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.model.TimeData;
import com.example.productivityapp.model.TimeDataFormatted;
import com.example.productivityapp.model.ToDo;
import com.example.productivityapp.model.User;
import com.example.productivityapp.model.UserTimesManager;
import com.example.productivityapp.presentacion.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SocialFragment extends Fragment {
    private AppDatabase appDatabase;
    private TextView txAcabadas;
    private TextView txNoAcabadas;
    private FirebaseAuth mAuth;
    private TextView txtUserEmail;
    private ImageButton btAddFriends;
    private Button logoutButton;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private TimeTableAdapter timeTableAdapter;
    private List<TimeDataFormatted> timeDataList;

    public SocialFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_social, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        timeDataList = new ArrayList<>();
        timeTableAdapter = new TimeTableAdapter(timeDataList);
        recyclerView.setAdapter(timeTableAdapter);

        appDatabase = AppDatabase.getDatabase();
        txAcabadas = root.findViewById(R.id.txNumTareasAcabadas);
        txNoAcabadas = root.findViewById(R.id.txNumTareasNoAcabadas);
        logoutButton = root.findViewById(R.id.logout);
        txtUserEmail = root.findViewById(R.id.txtUserEmail);
        btAddFriends = root.findViewById(R.id.btAddFriends);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        txtUserEmail.setText(Objects.requireNonNull(user).getEmail());

        // Retrieve data from Firebase and update the RecyclerView
        fetchDataFromFirebase();

        btAddFriends.setOnClickListener(v -> openFriendsActivity());
        logoutButton.setOnClickListener(v -> cerrarSesion());

        cargarValores();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        fetchDataFromFirebase();
    }

    private void openFriendsActivity() {
        Intent intent = new Intent(this.getActivity(), SearchFriendsActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this.getActivity()).toBundle());
    }

    private void fetchDataFromFirebase() {
        // Instantiate UserTimesManager
        UserTimesManager manager = new UserTimesManager();
        User userDb = AppDatabase.getDatabase().getUserDAO().getUser(user.getUid());
        List<String> friends = userDb.getFriends();
        friends.add(userDb.getEmail());
        List<TimeData> data = manager.getEmailAndTimesOfFriends(friends);
        timeDataList.clear();
        for (TimeData time: data) {
            String formattedSeconds = formatSecondsToHHmmss(time.getSeconds());
            timeDataList.add(new TimeDataFormatted(time.getEmail(),formattedSeconds));
        }
        timeTableAdapter.notifyDataSetChanged();

    }
    private String formatSecondsToHHmmss(long seconds) {
        int hours = (int) (seconds / 3600);
        int minutes = (int) ((seconds % 3600) / 60);
        int secs = (int) (seconds % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    private void cerrarSesion() {
        mAuth.signOut();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle());
        getActivity().finish();
    }

    private void cargarValores(){
        List<ToDo> tareasActivas = appDatabase.getTaskDAO().getAllActive(user.getUid(), "TO_DO");
        List<ToDo> tareasTerminadas = appDatabase.getTaskDAO().getAllFinished(user.getUid());
        txAcabadas.setText(String.valueOf(tareasTerminadas.size()));
        txNoAcabadas.setText(String.valueOf(tareasActivas.size()));
    }
}