package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordReAuth extends AppCompatActivity {


    private Button next;
    private TextView email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ImageView passwordVisibility;
    private boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_re_auth);

    next = findViewById(R.id.button_next_ChangePasswordReAuth);
    email = findViewById(R.id.textView_email_ChangePasswordReAuth);

    passwordVisibility = findViewById(R.id.imageView_passwordvisibility_ChangePasswordReAuth);


    mAuth = FirebaseAuth.getInstance();
    user = mAuth.getCurrentUser();

        email.setText(user.getEmail().toString());

    password = findViewById(R.id.editText_password_ChangePasswordReAuth);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //REAUTHENTICATION
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), password.getText().toString().trim());


                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    MoveToChangePassword();
                                } else {
                                    Toast.makeText(ChangePasswordReAuth.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        final Typeface typeface = ResourcesCompat.getFont(this,R.font.righteous);

        passwordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isVisible)
                {
                    isVisible  = true;
                    passwordVisibility.setBackgroundResource(R.drawable.eye);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setTypeface(typeface);


                }
                else
                {
                    isVisible = false;
                    passwordVisibility.setBackgroundResource(R.drawable.noeye);
                    password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTypeface(typeface);
                }
            }
        });
    }


    private void MoveToChangePassword()
    {
        Intent i = new Intent(this,ChangePassword.class);
        startActivity(i);

    }


}
