package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    public boolean AutoLogin = false;

    private String login = null;
    private String[] accountDetails = new String[2];   // do przekazania danych logowania
    private String password = null;
    private FirebaseAuth mAuth;
    private Button btnRegister;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private TextView textViewForgotPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        editTextLogin = findViewById(R.id.editText_login_Login);
        editTextPassword = findViewById(R.id.editText_password_Login);
        btnRegister = findViewById(R.id.buttom_register_Login);
        textViewForgotPassword = findViewById(R.id.textView_forgetpassword_Login);

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmplogin;
                if (editTextLogin != null) {
                    tmplogin = editTextLogin.getText().toString();
                } else {
                    tmplogin = " ";
                }
                MoveToForgotPassword(tmplogin);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToRegister();
            }
        });

        btnLogin = findViewById(R.id.button_login_Login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login = editTextLogin.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();
                if (CheckLoginData(login, password)) {
                    Login(login, password);


                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) then go to main menu
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            MoveToMainMenu();
        }
    }

    private void MoveToMainMenu() {
        Intent i = new Intent(this, MainMenu.class);

        startActivity(i);
    }

    private void MoveToForgotPassword(String emailField) {
        Intent i = new Intent(this, ForgotPassword.class);
        i.putExtra("email", emailField);
        startActivity(i);
    }

    private void MoveToRegister() {
        Intent i = new Intent(this, Register.class);
        startActivity(i);
    }

    private boolean CheckLoginData(String chLlogin, String chLpassword) {
        if (chLlogin != null && chLpassword != null) {

            return true;
        }
        return false;


    }

    private void Login(final String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Login completed", Toast.LENGTH_SHORT).show();

                    MoveToMainMenu();
                } else {
                    Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }


}
