package com.example.productivityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.productivityapp.model.Todo;
import com.google.android.material.snackbar.Snackbar;

import com.example.productivityapp.model.AppDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddTodoActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    private EditText txTitulo;
    private EditText txDescripcion;
    private EditText txFechaLimite;
    private Spinner spPrioridad;
    private Button btGuardar;
    private Button btCancelar;

    private Map<String, Todo.Priority> priorityMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        appDatabase = AppDatabase.getDatabase(this);
        loadPriorityMap();

        this.txTitulo = findViewById(R.id.editTxTitulo);
        this.txDescripcion = findViewById(R.id.editTxDescripcion);
        this.txFechaLimite = findViewById(R.id.editTxFecha);
        this.spPrioridad = findViewById(R.id.spPrioridad);
        this.btGuardar = findViewById(R.id.btGuardar);
        this.btCancelar = findViewById(R.id.btCancelar);

        this.btGuardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                añadirTarea();
            }
        });
        this.btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { cancelar(); }
        });
        cargarDatosSP();
    }

    private void loadPriorityMap() {
        Map<String, Todo.Priority> priorityMap = new HashMap<>();

        priorityMap.put("Baja", Todo.Priority.LOW);
        priorityMap.put("Media", Todo.Priority.MEDIUM);
        priorityMap.put("Alta", Todo.Priority.HIGH);

        this.priorityMap = priorityMap;
    }

    private void añadirTarea(){
        if (validarCampos()) {
            Todo tarea = new Todo(this.txTitulo.getText().toString(),
                    this.txDescripcion.getText().toString(),
                    LocalDate.now().plusDays(30),
                    priorityMap.get(spPrioridad.getSelectedItem().toString()),
                    false);
            this.appDatabase.getTaskDAO().add(tarea);

            Snackbar.make(findViewById(R.id.layoutAddTodo), "Tarea añadida",
                    Snackbar.LENGTH_LONG).show();

            Intent intentResultado = getIntent();
            setResult(RESULT_OK, intentResultado);
            finish();
        } else {
            Snackbar.make(findViewById(R.id.layoutAddTodo), "No se puede añadir la tarea",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private void cancelar(){
        Intent intentResultado = getIntent();
        setResult(RESULT_CANCELED, intentResultado);
        finish();
    }

    private boolean validarCampos(){
        if(this.txTitulo.getText().toString().isEmpty()) return false;
        if(this.txDescripcion.getText().toString().isEmpty()) return false;
        if(this.txFechaLimite.getText().toString().isEmpty()) return false;

        return true;
    }

    private void cargarDatosSP(){
        ArrayList<String> prioridad = new ArrayList<>();
        prioridad.add("Baja");
        prioridad.add("Media");
        prioridad.add("Alta");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, prioridad);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPrioridad.setAdapter(adapter);
    }
}