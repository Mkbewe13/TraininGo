package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordRepeat;
    private boolean emailIsValid = false;
    private boolean passwordsAreValid = false;
    private boolean checkPasswordsSyncHelper;//if true editTextPassword is valid and all we need to check is if editTextPasswordRepeat is same as editTextPassword
    private EditText editTextName;
    private ImageView imageViewPasswordVisibility;
    private ImageView imageViewPassword2Visibility;
    private boolean changePasswordVisibilityHelper = false;
    private boolean changePassword2VisibilityHelper = false;
    private Button buttonRegister;

    private TextView textViewRedHintEmail;
    private TextView textViewRedHintPassword;
    private TextView textViewRedHintPassword2;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();


        textViewRedHintEmail = findViewById(R.id.textView_redhintemail_Register);
        textViewRedHintPassword = findViewById(R.id.textView_redhintpassword_Register);
        textViewRedHintPassword2 = findViewById(R.id.textView_redhintpassword2_Register);
        editTextEmail = (EditText) findViewById(R.id.editText_email_Register);
        editTextPassword = (EditText) findViewById(R.id.editText_password_Register);
        editTextPasswordRepeat = (EditText) findViewById(R.id.editText_password2_Register);
        editTextName = (EditText) findViewById(R.id.editText_name_Register);
        imageViewPasswordVisibility = findViewById(R.id.imageView_passwordvisibility_Register);
        imageViewPassword2Visibility = findViewById(R.id.imageView_password2visibility_Register);
        buttonRegister = (Button) findViewById(R.id.button_register_Register);


        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CheckLogin(s.toString())) {
                    editTextEmail.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                    emailIsValid = true;
                } else {
                    editTextEmail.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                    emailIsValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });


        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CheckPassword(s.toString())) {
                    checkPasswordsSyncHelper = true;
                    editTextPassword.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                } else {
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

                if (checkPasswordsSyncHelper && CheckBothPasswords(s.toString(), editTextPassword.getText().toString())) {
                    editTextPasswordRepeat.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                    passwordsAreValid = true;
                } else {
                    editTextPasswordRepeat.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                    passwordsAreValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextName.getText().length() > 0) {
                    editTextName.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                } else {
                    editTextName.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final Typeface typeface = ResourcesCompat.getFont(this, R.font.righteous);


        // Make passwords visible or invisible
        imageViewPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!changePasswordVisibilityHelper) {
                    changePasswordVisibilityHelper = true;
                    imageViewPasswordVisibility.setBackgroundResource(R.drawable.eye);
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editTextPassword.setTypeface(typeface);


                } else {
                    changePasswordVisibilityHelper = false;
                    imageViewPasswordVisibility.setBackgroundResource(R.drawable.noeye);
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editTextPassword.setTypeface(typeface);
                }
            }
        });


        imageViewPassword2Visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!changePassword2VisibilityHelper) {
                    changePassword2VisibilityHelper = true;
                    imageViewPassword2Visibility.setBackgroundResource(R.drawable.eye);
                    editTextPasswordRepeat.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editTextPasswordRepeat.setTypeface(typeface);


                } else {
                    changePassword2VisibilityHelper = false;
                    imageViewPassword2Visibility.setBackgroundResource(R.drawable.noeye);
                    editTextPasswordRepeat.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editTextPasswordRepeat.setTypeface(typeface);
                }


            }
        });


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckFormValidation()) {

                    CreateUser(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim(), editTextName.getText().toString().trim());
                    //RegisterAndMoveToMainMenu(editTextName.getText().toString());
                }
            }
        });
    }

    private boolean CheckPassword(String chpassword) {

        boolean okPassword = true;
        if (chpassword.matches(".{8,}"))   //czy jest 8+ znakow
        {
            textViewRedHintPassword.setVisibility(View.INVISIBLE);
            if (chpassword.matches(".*\\d.*"))//czy ma cyfre w haśle
            {
                textViewRedHintPassword.setVisibility(View.INVISIBLE);
                {
                    if (chpassword.matches(".*[A-Z].*"))  //czy ma duza litere
                    {
                        textViewRedHintPassword.setVisibility(View.INVISIBLE);
                        if (chpassword.matches(".*[,./;'\\\\\\[\\]\\{\\}_+\\-=!@#$%^&*()~`<>?|\":;'].*")) //czy ma znak
                        {
                            textViewRedHintPassword.setVisibility(View.INVISIBLE);
                        } else {
                            okPassword = false;

                            textViewRedHintPassword.setText(R.string.register_without_specchar_passwd);
                            textViewRedHintPassword.setVisibility(View.VISIBLE);
                        }
                    } else {
                        okPassword = false;

                        textViewRedHintPassword.setText(R.string.register_without_uppercase_passwd);
                        textViewRedHintPassword.setVisibility(View.VISIBLE);
                    }

                }
            } else {
                okPassword = false;

                textViewRedHintPassword.setText(R.string.register_without_numbers_passwd);
                textViewRedHintPassword.setVisibility(View.VISIBLE);
            }
        } else {
            okPassword = false;


            textViewRedHintPassword.setText(R.string.register_too_short_passwd);
            textViewRedHintPassword.setVisibility(View.VISIBLE);
        }


        return okPassword;

    }

    private boolean CheckLogin(String chlogin) {
        if (chlogin.matches(".+[@].+[.].+")) {
            textViewRedHintEmail.setVisibility(View.INVISIBLE);
            return true;
        } else {

            textViewRedHintEmail.setText(R.string.register_wrongformat_email);
            textViewRedHintEmail.setVisibility(View.VISIBLE);
            return false;
        }
    }

    private boolean CheckBothPasswords(String password, String passwordRepeat) {
        if (password.equals(passwordRepeat)) {
            textViewRedHintPassword2.setVisibility(View.INVISIBLE);

            return true;
        } else
            textViewRedHintPassword2.setText(R.string.register_notsame_passwd);
        textViewRedHintPassword2.setVisibility(View.VISIBLE);
        return false;
    }


    private boolean CheckFormValidation() {
        if (editTextName.getText().length() == 0) {
            editTextName.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
            return false;
        } else {
            if (emailIsValid && passwordsAreValid) {
                return true;
            } else {
                return false;
            }


        }


    }


    // create new user in firebase auth system
    private void CreateUser(final String email, String password, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Welcome aboard " + email + ". Your account has been created successfully.", Toast.LENGTH_SHORT).show();
                    FirebaseUser mUser = mAuth.getCurrentUser();
                    CreateUserInDataBase(email, name, mUser.getUid());
                    Intent intent = new Intent(Register.this, MainMenu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(Register.this, task.getException().toString() + "  " + email, Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    // create new user in firebase cloud firestore, userID from firebase auth is new firestore document's name/id
    private void CreateUserInDataBase(String email, String name, String userID) {
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("name", name);
        newUser.put("age", "none");
        newUser.put("email", email);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userID)
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "Error writing document", e);
                        Toast.makeText(Register.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                });


        for (int i = 1; i <= 4; i++) {
            Map<String, Object> trainingPlansSlots = new HashMap<>();
            trainingPlansSlots.put("slot", String.valueOf(i));
            trainingPlansSlots.put("status", "empty");
            db.collection("users").document(userID).collection("trainingPlans").add(trainingPlansSlots);
        }


    }


}
