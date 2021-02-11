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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {


    private EditText password1;
    private EditText password2;
    private Button changePassword;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView textViewRedHintPassword;
    private boolean password1OK =false;

    private ImageView password1Visibility;
    private ImageView password2Visibility;
    private boolean pass1IsVisible = false;
    private boolean pass2IsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        textViewRedHintPassword = findViewById(R.id.textView_redhintpassword_ChangePassword);
        password1 = findViewById(R.id.editText_password1_ChangePassword);
        password2 = findViewById(R.id.editText_password2_ChangePassword);
        changePassword = findViewById(R.id.button_changePassword_ChangePassword);


        password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(CheckPassword(password1.getText().toString()))
                {
                    password1.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                    password1OK = true;
                }
                else
                {
                    password1.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                    password1OK = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(CheckBothPasswords(password1.getText().toString(),password2.getText().toString()))
                {
                    password2.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                }
                else
                {
                    password2.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        password1Visibility = findViewById(R.id.imageView_passwordvisibility_ChangePassword);
        password2Visibility = findViewById(R.id.imageView_passwordvisibility2_ChangePassword);
        final Typeface typeface = ResourcesCompat.getFont(this,R.font.righteous);

        password1Visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pass1IsVisible)
                {
                    pass1IsVisible= true;
                    password1Visibility.setBackgroundResource(R.drawable.eye);
                    password1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password1.setTypeface(typeface);


                }
                else
                {
                    pass1IsVisible= false;
                    password1Visibility.setBackgroundResource(R.drawable.noeye);
                    password1.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password1.setTypeface(typeface);
                }
            }
        });

        password2Visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pass2IsVisible)
                {
                    pass2IsVisible= true;
                    password2Visibility.setBackgroundResource(R.drawable.eye);
                    password2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password2.setTypeface(typeface);


                }
                else
                {
                    pass2IsVisible= false;
                    password2Visibility.setBackgroundResource(R.drawable.noeye);
                    password2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password2.setTypeface(typeface);
                }
            }
        });


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckBothPasswords(password1.getText().toString(),password2.getText().toString()))
                {
                    user.updatePassword(password1.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePassword.this,"Password changed successfully.",Toast.LENGTH_SHORT).show();
                                        MoveToProfile();
                                    }
                                }
                            });
                }
            }
        });






    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,Profile.class);
        startActivity(i);
    }


    private boolean CheckPassword(String chpassword)
    {
        boolean okPassword = true;
        if(chpassword.matches(".{8,}"))   //czy jest 8+ znakow
        {
            textViewRedHintPassword.setVisibility(View.INVISIBLE);
            if(chpassword.matches(".*\\d.*"))//czy ma cyfre w haśle
            {
                textViewRedHintPassword.setVisibility(View.INVISIBLE);
                {
                    if(chpassword.matches(".*[A-Z].*"))  //czy ma duza litere
                    {
                        textViewRedHintPassword.setVisibility(View.INVISIBLE);
                        if(chpassword.matches(".*[,./;'\\\\\\[\\]\\{\\}_+\\-=!@#$%^&*()~`<>?|\":;'].*")) //czy ma znak
                        {
                            textViewRedHintPassword.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            okPassword = false;

                            textViewRedHintPassword.setText(R.string.register_without_specchar_passwd);
                            textViewRedHintPassword.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        okPassword = false;

                        textViewRedHintPassword.setText(R.string.register_without_uppercase_passwd);
                        textViewRedHintPassword.setVisibility(View.VISIBLE);
                    }

                }
            }
            else
            {
                okPassword = false;

                textViewRedHintPassword.setText(R.string.register_without_numbers_passwd);
                textViewRedHintPassword.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            okPassword =false;


            textViewRedHintPassword.setText(R.string.register_too_short_passwd);
            textViewRedHintPassword.setVisibility(View.VISIBLE);
        }


        return okPassword;
    }


    private boolean CheckBothPasswords(String password1,String password2)
    {
        if(password1OK && password1.equals(password2))
        {
            return true;
        }
        return false;

    }

    private void MoveToProfile()
    {
        Intent i = new Intent(this,Profile.class);
        startActivity(i);
    }
}
