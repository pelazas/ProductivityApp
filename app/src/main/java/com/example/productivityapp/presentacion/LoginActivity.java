package com.example.productivityapp.presentacion;

import androidx.appcompat.app.AlertDialog;
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
import com.example.productivityapp.model.UserDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private TextView forgotPassword;
    private Button buttonLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView otherTxt;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        forgotPassword = findViewById(R.id.forgotPassword);
        buttonLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        otherTxt = findViewById(R.id.registerNow);

        otherTxt.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            finish();
        });

        forgotPassword.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Introduce tu email para restablecer contraseña");

            EditText input = new EditText(this);

            builder.setView(input);

            builder.setPositiveButton("Restablecer", (dialog, which) -> {
                mAuth.sendPasswordResetEmail(input.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Revisa tu correo electrónico", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            } else {
                                Toast.makeText(LoginActivity.this, "El correo proporcionado no existe", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        });

        buttonLogin.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            email = txtEmail.getText().toString();
            password = txtPassword.getText().toString();

            if (email.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Tienes que rellenar el campo de email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Tienes que rellenar el campo de contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                            Toast.makeText(LoginActivity.this, "Has iniciado sesión con éxito.",
                                    Toast.LENGTH_SHORT).show();
                            UserDAO userDao = AppDatabase.getDatabase().getUserDAO();

                            // No data of user in DB
                            if (userDao.getUser(mAuth.getUid()).getEmail().equals("NULL")) {
                                userDao.registerUser(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getEmail());
                            }

                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email o contraseña erróneos.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}