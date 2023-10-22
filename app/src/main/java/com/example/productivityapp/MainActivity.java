package com.example.productivityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

//HACE LA FUNCIÓN DE ACTIVITY DE TIMER
public class MainActivity extends AppCompatActivity {

    private static int STUDY_TIME = 25;
    private static int BREAK_TIME = 5;

    private CountDownTimer timer;
    private TextView timerText;
    private Button timerButton;
    private SwitchCompat modoSwitch;
    private TextView modoText;
    private BottomNavigationView navView;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargarDatosSP();

        navView = findViewById(R.id.bottom_nav_view);
        timerText = findViewById(R.id.timer_text);
        timerButton = findViewById(R.id.timer_button);
        modoSwitch = findViewById(R.id.modo_switch);
        modoText = findViewById(R.id.modo_text);

        if (modoSwitch.isChecked()) {
            switchBreakMode();
        } else {
            switchStudyMode();
        }

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimerRunning) {
                    timer.cancel();
                    isTimerRunning = false;
                    timerButton.setText(R.string.iniciarTextoBoton);
                } else {
                    startTimer(modoSwitch.isChecked() ? MainActivity.BREAK_TIME : MainActivity.STUDY_TIME);
                    isTimerRunning = true;
                    timerButton.setText(R.string.detenerTextoBoton);
                }
            }
        });

        modoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            }
        });
    }

    private void switchBreakMode() {
        timerText.setText(formatTime(MainActivity.BREAK_TIME, 0));
        modoText.setText(R.string.modo_descanso);
    }

    private void switchStudyMode() {
        timerText.setText(formatTime(MainActivity.STUDY_TIME, 0));
        modoText.setText(R.string.modo_estudio);
    }

    private void cargarDatosSP(){
        Spinner sp = findViewById(R.id.spAsignaturas);
        ArrayList<String> asignaturas = new ArrayList<>();
        asignaturas.add("Matematicas");
        asignaturas.add("Lengua");
        asignaturas.add("Física");
        asignaturas.add("Historia");
        // Se crea el adaptador de arrays
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, asignaturas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Aplicar el adapter al spinner
        sp.setAdapter(adapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        /* Cuando se selecciona uno de los botones / ítems*/
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_timer){
                /*TimerFragment timer = TimerFragment.newInstance(null);
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.timerFragment, timer).commit();*/
                return true;
            }else if (itemId == R.id.nav_toDo){
                /*ToDoFragment toDo = ToDoFragment.newInstance(null);
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.toDoFragment, toDo).commit()*/
                return true;
            }else if (itemId == R.id.nav_social){
                /*SocialFragment social = SocialFragment.newInstance(null);
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.socialFragment, social).commit();*/
                return true;
            }
            throw new IllegalStateException("Unexpected value: " + item.getItemId());
        };
    };

    private void startTimer(int minutes) {
        Log.i("INFO", String.format("Minutes: %d", minutes));
        timer = new CountDownTimer(minutes * 60 * 1000 + 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                timerText.setText(formatTime(minutes, seconds));
            }

            @Override
            public void onFinish() {
                timerText.setText("00:00");
                isTimerRunning = false;
                timerButton.setText(R.string.iniciarTextoBoton);
            }
        }.start();
    }

    private String formatTime(long minutes, long seconds) {
        return String.format("%02d:%02d", minutes, seconds);
    }
}