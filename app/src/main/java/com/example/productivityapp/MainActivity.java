package com.example.productivityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.productivityapp.utils.Formatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static int STUDY_TIME = 25;
    private static int BREAK_TIME = 5;

    private CountDownTimer timer;
    private TextView timerText;
    private Button timerButton;
    private SwitchCompat modoSwitch;
    private TextView modoText;
    private BottomNavigationView navView;
    private ProgressBar circularProgressBar;

    private boolean isTimerRunning = false;
    private int timerDuration;
    private int timeElapsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circularProgressBar = initializeProgressBar();

        cargarDatosSP();

        navView = findViewById(R.id.bottom_nav_view);
        timerText = findViewById(R.id.timer_text);
        timerButton = findViewById(R.id.timer_button);
        modoSwitch = findViewById(R.id.modo_switch);
        modoText = findViewById(R.id.modo_text);

        if (modoSwitch.isActivated()) {
            switchBreakMode();
        } else {
            switchStudyMode();
        }

        navView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        timerButton.setOnClickListener(view -> {
            if (isTimerRunning) {
                timer.cancel();
                isTimerRunning = false;
                timerButton.setText(R.string.iniciarTextoBoton);
            } else {
                startTimer(modoSwitch.isActivated() ? MainActivity.BREAK_TIME : MainActivity.STUDY_TIME);
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
        ProgressBar pb = findViewById(R.id.circularProgressBar);
        timerDuration = STUDY_TIME * 60;

        pb.setMax(timerDuration);
        pb.setProgress(timerDuration);
        pb.setRotation(0);

        return pb;
    }

    private void switchBreakMode() {
        timerText.setText(Formatter.formatTime(MainActivity.BREAK_TIME, 0));
        modoText.setText(R.string.modo_descanso);
        timerDuration = BREAK_TIME * 60;

        circularProgressBar.setMax(timerDuration);
        circularProgressBar.setProgress(timerDuration);
    }

    private void switchStudyMode() {
        timerText.setText(Formatter.formatTime(MainActivity.STUDY_TIME, 0));
        modoText.setText(R.string.modo_estudio);
        timerDuration = STUDY_TIME * 60;

        circularProgressBar.setMax(timerDuration);
        circularProgressBar.setProgress(timerDuration);
    }

    private void cargarDatosSP(){
        Spinner sp = findViewById(R.id.spAsignaturas);

        ArrayList<String> asignaturas = new ArrayList<>();
        asignaturas.add("Matemáticas");
        asignaturas.add("Lengua");
        asignaturas.add("Física");
        asignaturas.add("Historia");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, asignaturas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);
    }

    /* Cuando se selecciona uno de los botones / ítems*/
    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_timer){
                    return true;
                } else if (itemId == R.id.nav_toDo){
                    startActivity(new Intent(this, ToDoActivity.class));
                    return true;
                } else if (itemId == R.id.nav_social){
                    return true;
                } else {
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
            };

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
}