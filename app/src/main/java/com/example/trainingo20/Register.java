package com.example.trainingo20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPasswordRepeat;
    boolean emailIsValid = false;
    boolean passwordsAreValid = false;
    boolean nameIsValid = false;
    boolean checkPasswordsSyncHelper;//if true editTextPassword is valid and all we need to check if editTextPasswordRepeat is same as editTextPassword
    EditText editTextName;
    ImageView imageViewPasswordVisibility;
    ImageView imageViewPassword2Visibility;
    boolean changePasswordVisibilityHelper = false;
    boolean changePassword2VisibilityHelper = false;
    Button buttonRegister;

    TextView textViewRedHintEmail;
    TextView textViewRedHintPassword;
    TextView textViewRedHintPassword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);





        textViewRedHintEmail = findViewById(R.id.textView_redhintemail_Register);
        textViewRedHintPassword = findViewById(R.id.textView_redhintpassword_Register);
        textViewRedHintPassword2 = findViewById(R.id.textView_redhintpassword2_Register);

        editTextEmail = (EditText) findViewById(R.id.editText_email_Register);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(CheckLogin(s.toString()))
                {
                    editTextEmail.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                    emailIsValid = true;
                }
                else
                {
                    editTextEmail.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                    emailIsValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }




        });

        editTextPassword = (EditText) findViewById(R.id.editText_password_Register);
        editTextPasswordRepeat = (EditText) findViewById(R.id.editText_password2_Register);

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(CheckPassword(s.toString()))
                {
                    checkPasswordsSyncHelper = true;
                    editTextPassword.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                }
                else
                {
                    checkPasswordsSyncHelper = false;
                    editTextPassword.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


            editTextPasswordRepeat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(checkPasswordsSyncHelper && CheckBothPasswords(s.toString(),editTextPassword.getText().toString()))
                    {
                        editTextPasswordRepeat.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                        passwordsAreValid = true;
                    }
                    else
                    {
                        editTextPasswordRepeat.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                        passwordsAreValid = false;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        editTextName = (EditText) findViewById(R.id.editText_name_Register);

editTextName.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(editTextName.getText().length() > 0)
            {
                editTextName.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
            }
            else
            {
                editTextName.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
            }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
});


            final Typeface typeface = ResourcesCompat.getFont(this,R.font.righteous);




        imageViewPasswordVisibility = findViewById(R.id.imageView_passwordvisibility_Register);
      //  imageViewPasswordVisibility.setBackgroundResource(R.drawable.noeye);


            imageViewPasswordVisibility.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!changePasswordVisibilityHelper)
                    {
                        changePasswordVisibilityHelper = true;
                        imageViewPasswordVisibility.setBackgroundResource(R.drawable.eye);
                        editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editTextPassword.setTypeface(typeface);


                    }
                    else
                    {
                        changePasswordVisibilityHelper = false;
                        imageViewPasswordVisibility.setBackgroundResource(R.drawable.noeye);
                        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextPassword.setTypeface(typeface);
                    }
                }
            });

        imageViewPassword2Visibility = findViewById(R.id.imageView_password2visibility_Register);
    //    imageViewPassword2Visibility.setBackgroundResource(R.drawable.noeye);

            imageViewPassword2Visibility.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!changePassword2VisibilityHelper)
                    {
                        changePassword2VisibilityHelper = true;
                        imageViewPassword2Visibility.setBackgroundResource(R.drawable.eye);
                        editTextPasswordRepeat.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        editTextPasswordRepeat.setTypeface(typeface);


                    }
                    else
                    {
                        changePassword2VisibilityHelper = false;
                        imageViewPassword2Visibility.setBackgroundResource(R.drawable.noeye);
                        editTextPasswordRepeat.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        editTextPasswordRepeat.setTypeface(typeface);
                    }


                }
            });


            buttonRegister = (Button) findViewById(R.id.button_register_Register);
            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(CheckFormValidation())
                    {
                        RegisterAndMoveToMainMenu(editTextName.getText().toString());
                    }
                }
            });
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

    private boolean CheckLogin(String chlogin)
    {
        if(chlogin.matches(".+[@].+[.].+"))
        {
            textViewRedHintEmail.setVisibility(View.INVISIBLE);
            return true;
        }
        else
        {

            textViewRedHintEmail.setText(R.string.register_wrongformat_email);
            textViewRedHintEmail.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private boolean CheckBothPasswords(String password, String passwordRepeat)
    {
        if(password.equals(passwordRepeat))
        {
            textViewRedHintPassword2.setVisibility(View.INVISIBLE);

            return true;
        }
        else
            textViewRedHintPassword2.setText(R.string.register_notsame_passwd);
        textViewRedHintPassword2.setVisibility(View.VISIBLE);
            return false;
    }


    private boolean CheckFormValidation()
    {
            if(editTextName.getText().length() == 0 )
            {
                editTextName.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                return false;
            }
            else {
                if (emailIsValid && passwordsAreValid)
                {
                    return true;
                }
                else
                {
                    return false;
                }


            }


    }

    private void RegisterAndMoveToMainMenu(String NameForToast)
    {
        Intent i = new Intent(this,MainMenu.class);
        i.putExtra("newAccountName",NameForToast);
        startActivity(i);

        String toastMessage = "Welcome aboard " + NameForToast + ". Your account has been created successfully.";
        Toast toast = Toast.makeText(this,toastMessage,Toast.LENGTH_SHORT);
        View view = toast.getView();
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        text.setTextSize(20);


        toast.show();

    }

}
