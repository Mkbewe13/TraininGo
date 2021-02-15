package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateNewPlan extends AppCompatActivity {

    private EditText planName, planAbout;
    private TextView nameRedHint, aboutRedHint;
    private Button nextStep;
    private boolean okForNextStep = true;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private int slot;
    private String[] dataExtras;
    private String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_plan);


        planAbout = findViewById(R.id.editText_about_CreateNewPlan);
        planName = findViewById(R.id.editText_planname_CreateNewPlan);

        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()) {
            if (extras.containsKey("slot")) {
                slot = extras.getInt("slot");
            }
            if (extras.containsKey("planName") && extras.containsKey("planAbout")) {
                planName.setText(extras.getString("planName"));
                planAbout.setText(extras.getString("planAbout"));
            }
        }


        aboutRedHint = findViewById(R.id.textView_redhintabout_CreateNewPlan);
        nameRedHint = findViewById(R.id.textView_redhintname_CreateNewPlan);
        aboutRedHint.setText(R.string.createNewPlan_wrong_about);
        nameRedHint.setText(R.string.createNewPlan_wrong_name);
        dataExtras = new String[3];


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        planName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!CheckName(s.toString())) {
                    planName.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                    nameRedHint.setVisibility(View.VISIBLE);
                    okForNextStep = false;
                } else {
                    planName.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                    nameRedHint.setVisibility(View.INVISIBLE);
                    okForNextStep = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        planAbout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!CheckAbout(s.toString())) {
                    planAbout.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                    aboutRedHint.setVisibility(View.VISIBLE);
                    okForNextStep = false;


                } else {
                    planAbout.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                    aboutRedHint.setVisibility(View.INVISIBLE);
                    okForNextStep = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nextStep = findViewById(R.id.button_next_CreateNewPlan);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (planAbout.getText().length() != 0 && planName.getText().length() != 0) {
                    dataExtras[0] = String.valueOf(slot);
                    dataExtras[1] = planName.getText().toString();
                    dataExtras[2] = planAbout.getText().toString();

                    // CreateNewPlanDocument();

                    MoveToNextStep(okForNextStep, dataExtras);
                }
            }
        });


    }


    private void MoveToNextStep(boolean ok, String[] data) {
        if (ok) {
            Intent i = new Intent(this, CreateNewPlanSecondStep.class);
            i.putExtra("dataStep1", data);
            startActivity(i);
        }
    }

    private boolean CheckName(String name) {

        if (name.length() <= 0 || name.length() > 18) {

            return false;

        } else if (name.matches(".*[,./;'\\\\\\[\\]\\{\\}_+\\-=!@#$%^&*()~`<>?|\":;'].*") || name.matches("\\s")) {
            return false;
        }

        return true;
    }


    private boolean CheckAbout(String about) {

        if (about.length() <= 0 || about.length() > 144) {

            return false;

        } else if (about.matches(".*[,/;'\\\\\\[\\]\\{\\}_+\\-=@#$%^&*()~`<>|\":;'].*") || about.matches("\\s")) {
            return false;
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        deleteUnsavedPlans();
        Intent i = new Intent(this, MyPlans.class);
        startActivity(i);
    }


    private void SetStatus()   //not ready
    {
        final CollectionReference colTrainingPlans = db.collection("users").document(user.getUid()).collection("trainingPlans");
        colTrainingPlans.whereEqualTo("slot", String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot myDocument : task.getResult()) {
                        //now use id of document
                        status = myDocument.getString("status");
                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void deleteUnsavedPlans() {


        final CollectionReference colTrainingPlans = db.collection("users").document(user.getUid()).collection("trainingPlans");

        colTrainingPlans.whereEqualTo("slot", String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot myDocument : task.getResult()) {
                        //now use id of document
                        if (myDocument.getString("status").equals("tmp")) {
                            colTrainingPlans.document(myDocument.getId())
                                    .update("name", FieldValue.delete(), "about", FieldValue.delete(), "status", "empty");

                            String idDoc = myDocument.getId();
                            colTrainingPlans.document(idDoc).collection("plan")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    document.getReference().delete();
                                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                                }
                                            } else {
                                                // Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });


                            //  Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
