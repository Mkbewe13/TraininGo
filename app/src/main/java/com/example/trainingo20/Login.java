package com.example.trainingo20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    public boolean AutoLogin = false;
    private CheckBox checkBoxAutologin;
    private String login = null;
    private String[] accountDetails = new String[2];   // do przekazania danych logowania
    private String password = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editTextLogin = (EditText) findViewById(R.id.editText_login_Login);
        final EditText editTextPassword = (EditText) findViewById(R.id.editText_password_Login);

        TextView textViewForgotPassword = findViewById(R.id.textView_forgetpassword_Login);
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String tmplogin;
                if(editTextLogin!=null)
                {
                    tmplogin = editTextLogin.getText().toString();
                }
                else{
                    tmplogin = " ";
                }
                MoveToForgotPassword(tmplogin);
            }
        });


        Button btnRegister = findViewById(R.id.buttom_register_Login);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToRegister();
            }
        });

        Button btnLogin = findViewById(R.id.button_login_Login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login = editTextLogin.getText().toString();
                password = editTextPassword.getText().toString();
                if(CheckLoginData(login,password))
                {
                    MoveToMainMenu();
                }
            }
        });
    }


    private void MoveToMainMenu()
    {
        Intent i = new Intent(this,MainMenu.class);

        startActivity(i);
    }

    private void MoveToForgotPassword(String emailField)
    {
        Intent i = new Intent(this,ForgotPassword.class);
        i.putExtra("email",emailField);
        startActivity(i);
    }

    private void MoveToRegister()
    {
        Intent i = new Intent(this,Register.class);
        startActivity(i);
    }

    private boolean CheckLoginData(String chLlogin , String chLpassword)
    {
      if(chLlogin != null || chLpassword != null)
      {
        if(chLlogin.equals("admin") && chLpassword.equals("admin")){
            return true;
        }
        else{
            System.out.println("wyjebka passow log " + chLlogin + " pas " + chLpassword);


           Toast toast = Toast.makeText(this,"Wrong login or password. Try again.", Toast.LENGTH_SHORT);
            View view = toast.getView();

                        //Gets the actual oval background of the Toast then sets the colour filter
           // view.getBackground().setColorFilter(YOUR_BACKGROUND_COLOUR, PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
            TextView text = view.findViewById(android.R.id.message);
           text.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
           text.setTextSize(20);
            toast.setGravity(Gravity.BOTTOM, 0, 50);/// tutaj da sie pewnie optymalnie jakos lepiej zeby sie skalowalo a nie 50 na sztywno

            toast.show();

            return false;
        }
      }
      System.out.println("wyjebka nullem");
      return false;
    }



}
