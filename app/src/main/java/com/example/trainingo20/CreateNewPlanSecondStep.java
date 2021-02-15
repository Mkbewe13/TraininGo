package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateNewPlanSecondStep extends AppCompatActivity {

    private Button nextStep;
    private ListView exercisesList;
    private ArrayAdapter<String> adapter;
    private Button addExercise;
    private int slot;
    private String planName;
    private String planAbout;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private ArrayList<String> exercises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_plan_second_step);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey("slot")) {
                slot = bundle.getInt("slot");

            } else if (bundle.containsKey("dataStep1")) {
                String[] dataFromExtras = bundle.getStringArray("dataStep1");
                slot = Integer.parseInt(dataFromExtras[0]);
                planName = dataFromExtras[1];
                planAbout = dataFromExtras[2];
                AddNewPlanDataFromStep1();
            }
        }
        SetExtras();

        exercisesList = (ListView) findViewById(R.id.listView_exercises_CreateNewPlanSecondStep);
        LoadExercisesFromDB();


        exercisesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                // delete clicked exercise here and if the user clicks "no" in the next window, the exercise will be added again. Solve slow reload problem
                DeleteExercise(name);
                MoveToDeleteExerciseFromCreatorList(name);

            }


        });


        addExercise = findViewById(R.id.button_add_CreateNewPlanSecondStep);
        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToAddExercise();
            }
        });


        nextStep = findViewById(R.id.button_next_CreateNewPlanSecondStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MoveToCreateNewPlanThirdStep();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, CreateNewPlan.class);
        i.putExtra("slot", slot);
        i.putExtra("planName", planName);
        i.putExtra("planAbout", planAbout);
        startActivity(i);
    }

    private void MoveToAddExercise() {
        Intent i = new Intent(this, AddExercise.class);
        i.putExtra("slot", slot);
        startActivity(i);
    }

    private void MoveToCreateNewPlanThirdStep() {
        Intent i = new Intent(this, CreateNewPlanThirdStep.class);
        i.putExtra("slot", slot);
        i.putExtra("planName", planName);
        startActivity(i);
    }

    private void AddNewPlanDataFromStep1() {


        final Map<String, Object> newData = new HashMap<>();
        newData.put("name", planName);
        newData.put("about", planAbout);
        newData.put("status", "tmp");

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

    private void LoadExercisesFromDB() {

        final CollectionReference colTrainingPlans = db.collection("users").document(user.getUid()).collection("trainingPlans");

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
                                exercises = new ArrayList<String>();
                                if (task.isSuccessful()) {

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        exercises.add(document.getString("name"));
                                    }


                                }
                                adapter = new ArrayAdapter<String>(CreateNewPlanSecondStep.this, R.layout.row_choose_training, exercises);
                                exercisesList.setAdapter(adapter);
                            }
                        });

                        //  Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void MoveToDeleteExerciseFromCreatorList(final String exName) {
        Intent i = new Intent(this, DeleteExerciseFromCreatorList.class);
        String[] deleteData = new String[2];
        deleteData[0] = String.valueOf(slot);
        deleteData[1] = exName;
        i.putExtra("deleteData", deleteData);
        startActivity(i);


    }


    private void DeleteExercise(final String exName) {
        final CollectionReference colTrainingPlans = db.collection("users").document(user.getUid()).collection("trainingPlans");

        colTrainingPlans.whereEqualTo("slot", String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot myDocument : task.getResult()) {    //myDocumenr to dokument slotu na którym pracuje
                        //now use id of document

                        colTrainingPlans.document(myDocument.getId()).collection("plan").whereEqualTo("name", exName).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    String docId = myDocument.getId();

                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if (task.isSuccessful()) {

                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                document.getReference().delete();


                                            }

                                        }

                                    }
                                });

                        //  Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }


    private void SetExtras() {
        final CollectionReference colTrainingPlans = db.collection("users").document(user.getUid()).collection("trainingPlans");

        colTrainingPlans.whereEqualTo("slot", String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot myDocument : task.getResult()) {
                        //now use id of document

                        planAbout = myDocument.getString("about");
                        planName = myDocument.getString("name");

                        //  Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

}



