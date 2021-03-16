package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditPlanFirstStep extends AppCompatActivity {

    private boolean okForNextStep = true;
    private EditText editTextName,editTextAbout;
    private Button buttonNext,buttonCancel,buttonSaveAndExit;
    private TextView nameRedHint, aboutRedHint;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
private int slot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan_first_step);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        editTextAbout = findViewById(R.id.editText_about_EditPlanFirstStep);
        editTextName = findViewById(R.id.editText_planname_EditPlanFirstStep);

        buttonCancel  = findViewById(R.id.button_cancel_EditPlanFirstStep);
        buttonNext = findViewById(R.id.button_next_EditPlanFirstStep);
        buttonSaveAndExit = findViewById((R.id.button_saveAndExit_EditPlanFirstStep));

        nameRedHint = findViewById(R.id.textView_redhintname_EditPlanFirstStep);
        aboutRedHint = findViewById(R.id.textView_redhintabout_EditPlanFirstStep);


        Bundle extras = getIntent().getExtras();
            if(!extras.isEmpty())
            {
                if(extras.containsKey("slot"))
                {
                    slot = extras.getInt("slot");
                }
            }


            db.collection("users").document(user.getUid()).collection("trainingPlans").whereEqualTo("slot",String.valueOf(slot)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot qds : task.getResult())
                        {

                            editTextName.setText(qds.getString("name"));
                            editTextAbout.setText(qds.getString("about"));
                        }
                    }
                }
            });


            editTextName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!CheckName(s.toString())) {
                        editTextName.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                        nameRedHint.setVisibility(View.VISIBLE);
                        okForNextStep = false;
                    } else {
                        editTextName.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                        nameRedHint.setVisibility(View.INVISIBLE);
                        okForNextStep = true;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            editTextAbout.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!CheckAbout(s.toString())) {
                        editTextAbout.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                        aboutRedHint.setVisibility(View.VISIBLE);
                        okForNextStep = false;


                    } else {
                        editTextAbout.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                        aboutRedHint.setVisibility(View.INVISIBLE);
                        okForNextStep = true;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });





            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(EditPlanFirstStep.this,MyPlans.class);
                    startActivity(i);
                }
            });

            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextAbout.getText().length() != 0 && editTextName.getText().length() != 0) {

                        AddNewPlanDataFromStep1();
                        // CreateNewPlanDocument();

                        MoveToNextStep(okForNextStep, slot);
                    }
                }
            });

            buttonSaveAndExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              AddNewPlanDataFromStep1();
                    Intent i = new Intent(EditPlanFirstStep.this,MyPlans.class);
                    startActivity(i);
                }
            });

    }


    private void MoveToNextStep(boolean ok, int slot) {
        if (ok) {
            Intent i = new Intent(this, EditPlanSecondStep.class);
            i.putExtra("slot", slot);
            startActivity(i);
        }
    }


    private void AddNewPlanDataFromStep1() {


        final Map<String, Object> newData = new HashMap<>();
        newData.put("name", editTextName.getText().toString());
        newData.put("about", editTextAbout.getText().toString());
        newData.put("status", "taken");

        final CollectionReference colTrainingPlans = db.collection("users").document(user.getUid()).collection("trainingPlans");

        colTrainingPlans.whereEqualTo("slot", String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot myDocument : task.getResult()) {
                        //now use id of document

                        colTrainingPlans.document(myDocument.getId()).update(newData);

                        //  Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
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

}
