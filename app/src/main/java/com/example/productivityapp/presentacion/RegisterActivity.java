package com.example.productivityapp.presentacion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productivityapp.R;
import com.example.productivityapp.model.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtRepeatPassword;
    private Button buttonRegister;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView otherTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtRepeatPassword = findViewById(R.id.txtRepeatPassword);
        buttonRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        otherTxt = findViewById(R.id.loginNow);

        otherTxt.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

        buttonRegister.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            String repeatPassword = txtRepeatPassword.getText().toString();

            if (email.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Tienes que rellenar el campo de email.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Tienes que rellenar el campo de contraseña.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 8) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "La contraseña debe tener mínimo 8 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (repeatPassword.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Tienes que rellenar el campo de repetir contraseña.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(repeatPassword)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "La contraseña y la contraseña repetida deben coincidir.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Cuenta creada con éxito.",
                                    Toast.LENGTH_SHORT).show();
                            AppDatabase.getDatabase().getUserDAO().registerUser(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getEmail());
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Ya existe una cuenta con ese correo electrónico / email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}