package com.example.trainingo20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EditText editTextEmail= findViewById(R.id.editText_email_ForgotPassword);
        Bundle extras = getIntent().getExtras();
        if(extras!= null)
        {
            editTextEmail.setText(extras.getString("email"));
        }

    }
}
