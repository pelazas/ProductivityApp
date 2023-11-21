package com.example.productivityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.productivityapp.model.Todo;
import com.google.android.material.snackbar.Snackbar;

import com.example.productivityapp.model.AppDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTodoActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    private EditText txTitulo;
    private EditText txDescripcion;
    private EditText txFechaLimite;
    private Spinner spPrioridad;
    private Button btGuardar;
    private Button btBorrar;

    private Todo todo;

    private Map<String, Todo.Priority> priorityMap;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        this.btBorrar = findViewById(R.id.btBorrar);

        this.btGuardar.setOnClickListener(view -> addTarea());

        cargarDatosSP();

        Intent intent = getIntent();
        todo = intent.getParcelableExtra(ToDoFragment.TODO_SELECTED);

        if (todo != null) {
            fillData();
        }
        activeButtonBorrar();
    }

    private void loadPriorityMap() {
        Map<String, Todo.Priority> priorityMap = new HashMap<>();

        priorityMap.put("Baja", Todo.Priority.LOW);
        priorityMap.put("Media", Todo.Priority.MEDIUM);
        priorityMap.put("Alta", Todo.Priority.HIGH);

        this.priorityMap = priorityMap;
    }

    private void addTarea(){
        if (validarCampos()) {
            if (todo == null) {
                Todo tarea = new Todo(this.txTitulo.getText().toString(),
                        this.txDescripcion.getText().toString(),
                        LocalDate.parse(this.txFechaLimite.getText().toString(), formatter),
                        priorityMap.get(spPrioridad.getSelectedItem().toString()),
                        Todo.State.TO_DO);
                this.appDatabase.getTaskDAO().add(tarea);

                Snackbar.make(findViewById(R.id.layoutAddTodo), R.string.crear_tarea,
                        Snackbar.LENGTH_LONG).show();
            } else {
                todo.setTitle(this.txTitulo.getText().toString());
                todo.setDescription(this.txDescripcion.getText().toString());
                todo.setLimitDate(LocalDate.parse(this.txFechaLimite.getText().toString(), formatter));
                todo.setPriority(priorityMap.get(spPrioridad.getSelectedItem().toString()));

                this.appDatabase.getTaskDAO().update(todo);

                Snackbar.make(findViewById(R.id.layoutAddTodo), R.string.editar_tarea,
                        Snackbar.LENGTH_LONG).show();
            }


            Intent intentResultado = getIntent();
            setResult(RESULT_OK, intentResultado);
            finish();
        } else {
            int txt;

            if (!validarFecha(this.txFechaLimite.getText().toString())) {
                txt = R.string.error_campo_fecha;
            } else if (LocalDate.parse(this.txFechaLimite.getText().toString(), formatter).isBefore(LocalDate.now())) {
                txt = R.string.error_fecha_anterior;
            } else {
                txt = R.string.error_campos_tarea;
            }

            Snackbar.make(findViewById(R.id.layoutAddTodo), txt,
                    Snackbar.LENGTH_LONG).show();
        }
    }


    private void activeButtonBorrar(){
        if(todo == null) this.btBorrar.setVisibility(View.INVISIBLE);
        else this.btBorrar.setOnClickListener(view -> borrar());
    }
    private void borrar(){
        this.appDatabase.getTaskDAO().updateFromRemove(todo.getId(), "CANCEL");

        Snackbar.make(findViewById(R.id.layoutAddTodo), R.string.borrar_tarea,
                Snackbar.LENGTH_LONG).show();

        Intent intentResultado = getIntent();
        setResult(RESULT_CANCELED, intentResultado);
        finish();
    }

    private boolean validarCampos(){
        if (this.txTitulo.getText().toString().isEmpty()) return false;
        if (this.txDescripcion.getText().toString().isEmpty()) return false;
        if (this.txFechaLimite.getText().toString().isEmpty()) return false;
        if (!validarFecha(this.txFechaLimite.getText().toString())) return false;
        if (!validarFechaTiempoValido( LocalDate.parse(this.txFechaLimite.getText().toString(), formatter))) return false;

        return true;
    }

    private boolean validarFecha(String date) {
        try {
            LocalDate.parse(date, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarFechaTiempoValido(LocalDate date) {
        return !LocalDate.parse(this.txFechaLimite.getText().toString(), formatter).isBefore(LocalDate.now());
    }

    private void cargarDatosSP(){
        List<String> prioridad = new ArrayList<>();
        prioridad.add("Baja");
        prioridad.add("Media");
        prioridad.add("Alta");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, prioridad);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spPrioridad.setAdapter(adapter);
    }

    private void fillData() {
        ((TextView) findViewById(R.id.txTareaNueva)).setText(R.string.editar_tarea_titulo);
        txTitulo.setText(todo.getTitle());
        txDescripcion.setText(todo.getDescription());
        txFechaLimite.setText(todo.getLimitDate().format(formatter));

        int index = 0;

        if (todo.getPriority() == Todo.Priority.HIGH) {
            index = 2;
        } else if (todo.getPriority() == Todo.Priority.MEDIUM) {
            index = 1;
        }

        spPrioridad.setSelection(index);

    }
}