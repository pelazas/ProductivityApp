package com.example.productivityapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.model.Todo;
import com.example.productivityapp.utils.Formatter;

import java.util.ArrayList;
import java.util.List;

public class TimerFragment extends Fragment  implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int STUDY_TIME = 25;
    private static final int BREAK_TIME = 5;

    private CountDownTimer timer;
    private TextView timerText;
    private Button timerButton;
    private Switch modoSwitch;
    private ProgressBar circularProgressBar;
    private TextView modoText;
    private SharedPreferences sharedPreferences;

    private boolean isTimerRunning = false;
    private int timerDuration;
    private int timeElapsed;

    private List<Todo> todos;

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarDatosSP();

        timerText = view.findViewById(R.id.timer_text);
        timerButton = view.findViewById(R.id.timer_button);
        modoSwitch = view.findViewById(R.id.modo_switch);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        modoText = view.findViewById(R.id.modo_text);

        setupSharedPreferences();

        String color = sharedPreferences.getString("color_barra", "#1c7ed6");
        switchBarColor(color);

        circularProgressBar = initializeProgressBar();

        if (modoSwitch.isChecked()) {
            switchBreakMode();
        } else {
            switchStudyMode();
        }

        timerButton.setOnClickListener(v -> {
            if (isTimerRunning) {
                timer.cancel();
                isTimerRunning = false;
                timerButton.setText(R.string.iniciarTextoBoton);
            } else {
                startTimer(modoSwitch.isChecked() ? BREAK_TIME : STUDY_TIME);
                isTimerRunning = true;
                timerButton.setText(R.string.detenerTextoBoton);
            }
        });

        modoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (timer != null) {
                timer.cancel();
                isTimerRunning = false;
                timerButton.setText(R.string.iniciarTextoBoton);
            }

            if (isChecked) {
                switchBreakMode();
            } else {
                switchStudyMode();
            }
        });
    }

    private ProgressBar initializeProgressBar() {
        ProgressBar pb = getView().findViewById(R.id.circularProgressBar);
        timerDuration = STUDY_TIME * 60;

        pb.setMax(timerDuration);
        pb.setProgress(timerDuration);
        pb.setRotation(0);

        return pb;
    }

    private void switchBreakMode() {
        timerText.setText(Formatter.formatTime(BREAK_TIME, 0));
        modoText.setText(R.string.modo_descanso);
        timerDuration = BREAK_TIME * 60;

        circularProgressBar.setMax(timerDuration);
        circularProgressBar.setProgress(timerDuration);
    }

    private void switchStudyMode() {
        timerText.setText(Formatter.formatTime(STUDY_TIME, 0));
        modoText.setText(R.string.modo_estudio);
        timerDuration = STUDY_TIME * 60;

        circularProgressBar.setMax(timerDuration);
        circularProgressBar.setProgress(timerDuration);
    }
    private void cargarDatosSP(){
        Spinner sp = getView().findViewById(R.id.spAsignaturas);

        ArrayList<Todo> asignaturas = new ArrayList<>();

        todos = AppDatabase.getDatabase(requireContext()).getTaskDAO().getAll();
        for(int i = 0; i<todos.size();i++){
            asignaturas.add(todos.get(i));
        }

        ArrayAdapter<Todo> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, asignaturas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);
    }

    private void startTimer(int minutes) {
        timerDuration = minutes * 60;
        timeElapsed = timerDuration;

        timer = new CountDownTimer(minutes * 60 * 1000 + 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;

                timerText.setText(Formatter.formatTime(minutes, seconds));
                timeElapsed = (int) millisUntilFinished / 1000;
                circularProgressBar.setProgress(timeElapsed);
            }

            @Override
            public void onFinish() {
                timerText.setText("00:00");
                isTimerRunning = false;
                timerButton.setText(R.string.iniciarTextoBoton);
            }
        }.start();
    }

    private void setupSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getView().getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void switchBarColor(String color) {
        int colorParsed = Color.parseColor(color);

        RotateDrawable drawable = (RotateDrawable) circularProgressBar.getProgressDrawable();
        GradientDrawable shapeDrawable = (GradientDrawable) ((RotateDrawable) drawable).getDrawable();

        if (shapeDrawable != null) {
            shapeDrawable.setColors(new int[]{
                    colorParsed,
                    colorParsed
            });
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("color_barra")) {
            String color = sharedPreferences.getString("color_barra", "#1c7ed6");
            switchBarColor(color);
        }
    }
}
