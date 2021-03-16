package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditPlanSecondStep extends AppCompatActivity {

    private ListView listOfExercises;
    private Button savePlan;
    private Button cancel;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private int slot;
    private ExerciseVolumeAdapter adapter;
    private ArrayList<Exercise> exercisesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan_second_step);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle =  getIntent().getExtras();
        if(!bundle.isEmpty())
        {
            if(bundle.containsKey("slot"))
            {
                slot = bundle.getInt("slot");
            }
        }


        listOfExercises = findViewById(R.id.listView_exercises_EditPlanSecondStep);


        final CollectionReference colTrainingPlans = db.collection("users").document(user.getUid()).collection("trainingPlans");
        //set exercises list created in second step, with data from database
        colTrainingPlans.whereEqualTo("slot", String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot myDocument : task.getResult()) {
                        //now use id of document

                        colTrainingPlans.document(myDocument.getId()).collection("plan").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                exercisesList = new ArrayList<Exercise>();
                                if (task.isSuccessful()) {
                                    Exercise e;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        e = new Exercise(document.getString("name"));
                                        exercisesList.add(e);
                                    }


                                }
                                adapter = new ExerciseVolumeAdapter(EditPlanSecondStep.this, R.layout.row_create_plan_set_volume, exercisesList);
                                listOfExercises.setAdapter(adapter);

                            }
                        });

                        //  Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


        savePlan = findViewById(R.id.button_savePlan_EditPlanSecondStep);

        savePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckAllFieldsOk(adapter)) {
                    for (int i = 0; i < adapter.getCount(); i++) {

                        SaveDataToDB(adapter.getItem(i).getName(), adapter.getItem(i).getSeries(), adapter.getItem(i).getReps());
                    }
                    MoveBackToMyPlansAndSaveChanges();

                } else {

                    Toast.makeText(EditPlanSecondStep.this, "Check series and reps fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cancel = findViewById(R.id.button_cancel_EditPlanSecondStep);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditPlanSecondStep.this,MyPlans.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {

    }


    private void MoveBackToMyPlansAndSaveChanges() {
        final CollectionReference colTrainingPlans = db.collection("users").document(user.getUid()).collection("trainingPlans");



        colTrainingPlans.whereEqualTo("slot", String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot myDocument : task.getResult()) {
                        //now use id of document


                        myDocument.getReference().update("status", "taken");
                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        Intent i = new Intent(EditPlanSecondStep.this, MyPlans.class);

        startActivity(i);

    }


    private void SaveDataToDB(final String name, final int series, final int reps) {
        final CollectionReference colRef = db.collection("users").document(user.getUid()).collection("trainingPlans");

        colRef.whereEqualTo("slot", String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot myDoc : task.getResult()) {
                        Map<String, Object> data = new HashMap<>();


                        data.put("name", name);
                        data.put("series", series);
                        data.put("reps", reps);
                        myDoc.getReference().collection("plan").whereEqualTo("name", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentExercise : task.getResult()) {
                                        documentExercise.getReference().update("series", series, "reps", reps);

                                    }

                                }
                            }
                        });


                    }
                } else {
                    Toast.makeText(EditPlanSecondStep.this, task.getException().toString(), Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean CheckAllFieldsOk(ExerciseVolumeAdapter a) {


        for (int i = 0; i < a.getCount(); i++) {
            if (!a.getItem(i).getFieldOk()) {
                return false;
            }


        }
        return true;
    }
}
