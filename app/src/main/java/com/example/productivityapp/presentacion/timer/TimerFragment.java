package com.example.productivityapp.presentacion.timer;

import static android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
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

import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;
import com.example.productivityapp.model.ToDo;
import com.example.productivityapp.utils.Formatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimerFragment extends Fragment  implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String SIMPLE_CHANNEL = "Canal simple";
    private static final int DEFAULT_STUDY_TIME = 25;
    private static final int DEFAULT_BREAK_TIME = 5;

    private CountDownTimer timer;
    private TextView timerText;
    private Button timerButton;
    private Switch modoSwitch;
    private ProgressBar circularProgressBar;
    private TextView modoText;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private boolean isTimerRunning = false;
    private int timerDuration;
    private int timeElapsed;

    private List<ToDo> todos;

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
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        cargarDatosSP();

        timerText = view.findViewById(R.id.timer_text);
        timerButton = view.findViewById(R.id.timer_button);
        modoSwitch = view.findViewById(R.id.modo_switch);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        modoText = view.findViewById(R.id.modo_text);

        setupSharedPreferences();
        int initialStudyTime = getSelectedStudyTime();
        int initialBreakTime = getSelectedBreakTime();

        String color = sharedPreferences.getString("color_barra", "#1c7ed6");
        switchBarColor(color);

        circularProgressBar = initializeProgressBar(initialStudyTime);

        if (modoSwitch.isChecked()) {
            switchBreakMode();
        } else {
            switchStudyMode();
        }

        crearCanalNotificacion();
        timerButton.setOnClickListener(v -> {
            if (isTimerRunning) {
                timer.cancel();
                isTimerRunning = false;
                timerButton.setText(R.string.iniciarTextoBoton);
            } else {
                startTimer(modoSwitch.isChecked() ? initialBreakTime : initialStudyTime);
                isTimerRunning = true;
                timerButton.setText(R.string.detenerTextoBoton);
                crearNotificacionProgramada();
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

    private ProgressBar initializeProgressBar(int studyTime) {
        ProgressBar pb = getView().findViewById(R.id.circularProgressBar);
        timerDuration = studyTime * 60;

        pb.setMax(timerDuration);
        pb.setProgress(timerDuration);
        pb.setRotation(0);

        return pb;
    }

    private void crearCanalNotificacion() {
        //Si API 26 o superior.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(this.SIMPLE_CHANNEL,
                                    "Productivity App",
                                    NotificationManager.IMPORTANCE_DEFAULT);
            //Añadimos el canal al servicio de notificaciones.
            NotificationManager notificationManager = (NotificationManager) new Activity()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(canal);
        }
    }
    private void crearNotificacionProgramada() {
        String tiempo = String.valueOf(this.timerText.getText());
        long minutos = Long.parseLong(tiempo.split(":")[0]);

        Duration duration = Duration.ofMinutes(minutos);
        long milisegundos = duration.toMillis();

        Intent intent = new Intent(requireContext(), ProgramadaNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                ProgramadaNotification.PROGRAMADA_NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) new Activity()
                .getSystemService(Context.ALARM_SERVICE);
        //API 31+ y permiso para alarmas exactas o API < 31
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    Calendar.getInstance().getTimeInMillis() + milisegundos,pendingIntent);
        } else // 31 y sin permiso. Solicitamos
            startActivity(new Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM));
    }

    private void switchBreakMode() {
        int time = getSelectedBreakTime();
        timerText.setText(Formatter.formatTime(time, 0));
        modoText.setText(R.string.modo_descanso);
        timerDuration = time * 60;

        circularProgressBar.setMax(timerDuration);
        circularProgressBar.setProgress(timerDuration);
    }

    private void switchStudyMode() {
        int time = getSelectedStudyTime();
        timerText.setText(Formatter.formatTime(time, 0));
        modoText.setText(R.string.modo_estudio);
        timerDuration = time * 60;

        circularProgressBar.setMax(timerDuration);
        circularProgressBar.setProgress(timerDuration);
    }
    private void cargarDatosSP(){
        Spinner sp = getView().findViewById(R.id.spAsignaturas);

        ArrayList<ToDo> asignaturas = new ArrayList<>();

        todos = AppDatabase.getDatabase(requireContext()).getTaskDAO().getAll(user.getUid());

        for(int i = 0; i<todos.size();i++){
            if(!todos.get(i).getState().equals(ToDo.State.CANCEL))
                asignaturas.add(todos.get(i));
        }

        ArrayAdapter<ToDo> adapter = new ArrayAdapter<>(
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

    private int getSelectedStudyTime() {
        // Read the selected study time from SharedPreferences, use DEFAULT_STUDY_TIME if not found
        String studyTimeStr = sharedPreferences.getString("study_time", String.valueOf(DEFAULT_STUDY_TIME));
        return Integer.parseInt(studyTimeStr);
    }

    private int getSelectedBreakTime() {
        // Read the selected break time from SharedPreferences, use DEFAULT_BREAK_TIME if not found
        String breakTimeStr = sharedPreferences.getString("break_time", String.valueOf(DEFAULT_BREAK_TIME));
        return Integer.parseInt(breakTimeStr);
    }
}
