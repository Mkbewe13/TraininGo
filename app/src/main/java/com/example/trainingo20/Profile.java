package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {


    private Button signOut;
    private Button changePassword;
    private EditText name;
    private EditText age;
    private Button save;
    private FirebaseAuth mAuth;
    private TextView labelAge;
    private TextView email;
    private boolean changesToSave = false;
    private boolean firstLoadName = false;
    private boolean firstLoadAge = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();


        email =findViewById(R.id.textView_email_Profile);
        email.setText(mAuth.getCurrentUser().getEmail().toString().trim());


        labelAge = findViewById(R.id.textView_labelAge_Profile);
        name = findViewById(R.id.editText_name_Profile);
        age = findViewById(R.id.editText_age_Profile);
        save = findViewById(R.id.button_save_Profile);
        save.setVisibility(View.INVISIBLE);
        signOut = findViewById(R.id.button_signOut_Profile);
        changePassword = findViewById(R.id.button_changePassword_Profile);

        SetFieldsDatabaseData();

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!firstLoadName) {
                    ShowSave();
                    changesToSave = true;
                    if (!CheckName(name.getText().toString())) {
                        name.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                    } else
                        name.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);

                }
                else
                    firstLoadName=false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!firstLoadAge){
                    ShowSave();
                    changesToSave = true;
                    if (!CheckAge(age.getText().toString().trim())) {
                        age.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                    } else
                        age.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                }
                else
                    firstLoadAge = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changesToSave && CheckName(name.getText().toString())  && CheckAge(age.getText().toString()))
                {
                    //updatedanych w bazie
                    SaveToDatabase(name.getText().toString(),age.getText().toString());
                    HideSave();
                    changesToSave = false;
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(Profile.this,Login.class);
                startActivity(i);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MoveToChangePassword();

                 }
                });

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,MainMenu.class);
        startActivity(i);
    }

    private boolean CheckName(String name)
    {
        if(name.length() > 0)
        {
            return true;
        }
        else
            return false;
    }

    private boolean CheckAge(String age)
    {
        if( age.matches("0\\d|0") || !age.matches("\\d{1,2}"))
        {
            return false;
        }
        else
            return true;
    }


    private void ShowSave()
    {
        save.setVisibility(View.VISIBLE);
        signOut.setVisibility(View.INVISIBLE);
        changePassword.setVisibility(View.INVISIBLE);

    }

    private void HideSave()
    {
        save.setVisibility(View.INVISIBLE);
        signOut.setVisibility(View.VISIBLE);
        changePassword.setVisibility(View.VISIBLE);
    }

    private void MoveToChangePassword()
    {
        Intent i = new Intent(this,ChangePasswordReAuth.class);
        startActivity(i);

    }

    private void SaveToDatabase(String Name, String Age)
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(mAuth.getCurrentUser().getUid());
        documentReference.update("name",Name,"age",Age).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d(TAG, "DocumentSnapshot successfully updated!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.w(TAG, "Error updating document", e);
                Toast.makeText(Profile.this,e.toString(),Toast.LENGTH_SHORT);
            }
        });
    }

    private void SetFieldsDatabaseData()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(mAuth.getCurrentUser().getUid());
                firstLoadName =true;
                firstLoadAge = true;
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                     //   Log.d(TAG, "DocumentSnapshot data: " + document.getData());


                        age.setText(document.getString("age"));

                        name.setText(document.getString("name"));


                    } else {
                       // Log.d(TAG, "No such document");
                        Toast.makeText(Profile.this,"No such document",Toast.LENGTH_SHORT);
                    }
                } else {
                   // Log.d(TAG, "get failed with ", task.getException());
                    Toast.makeText(Profile.this,"get failed with "+task.getException(),Toast.LENGTH_SHORT);
                }
            }
        });

    }

}
