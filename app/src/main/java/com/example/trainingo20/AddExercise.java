package com.example.trainingo20;

import androidx.annotation.NonNull;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddExercise extends AppCompatActivity {

    private ListView exercisesList;
    private ArrayAdapter<String> adapter;
    private Button addOwnExercise;
    private FirebaseFirestore db;
    private ArrayList<String> exercises;
    private FirebaseUser user;
    private int slot;
    private boolean checkRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);


        //GET EXTRAS
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {

          slot = bundle.getInt("slot");
        }
        //

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        exercisesList = (ListView) findViewById(R.id.listView_exercises_AddExercise);


        //LOAD EXERCISES LIST FROM DB
       exercises = new ArrayList<String>();

        //FIRST - USER EXERCISES
        db.collection("users").document(user.getUid()).collection("userExercises").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot doc : task.getResult()) {

                        String tmp = doc.get("name").toString();
                        exercises.add(tmp.substring(0,1).toUpperCase() + tmp.substring(1).toLowerCase());


                    }

                }
            }
        });



        //THEN GLOBAL


        db.collection("exercisesGlobal")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Toast.makeText(Stats.this,"Success",Toast.LENGTH_LONG).show();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String tmp = document.get("name").toString();
                                exercises.add(tmp.substring(0,1).toUpperCase() + tmp.substring(1).toLowerCase());


                            }





                            adapter = new ArrayAdapter<String>(AddExercise.this, R.layout.row_add_exercise, exercises);
                            exercisesList.setAdapter(adapter);
                        } else {
                            Toast.makeText(AddExercise.this,task.getException().toString(),Toast.LENGTH_LONG).show();

                        }
                    }
                });


        exercisesList.setAdapter(adapter);
        //

        //CHECK IF EXERCISE IS ALREADY IN LIST AND ADD NEW IF FALSE
        exercisesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String name = (String) parent.getItemAtPosition(position);

                final CollectionReference colRef = db.collection("users").document(user.getUid()).collection("trainingPlans");

                colRef.whereEqualTo("slot",String.valueOf(slot)).limit(1)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for (final QueryDocumentSnapshot myDoc: task.getResult())
                            {

                                CollectionReference myCol = colRef.document(myDoc.getId()).collection("plan");
                                myCol.get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            String docId = myDoc.getId();
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    boolean check = false;
                                                    for (QueryDocumentSnapshot document : task.getResult()) {


                                                        if(document.getString("name").toLowerCase().equals(name.toLowerCase()))
                                                        {

                                                            check = true;
                                                        }

                                                    }
                                                            if(!check)
                                                            {
                                                                AddExerciseToDbPlan(name);
                                                                MoveBackToCreatePlanSecondStepExerciseAdded();
                                                            }
                                                            else
                                                            {
                                                                Toast.makeText(AddExercise.this,"You already have this exercise in your plan.",Toast.LENGTH_SHORT).show();
                                                                MoveBackToCreatePlanSecondStepExerciseAdded();
                                                            }

                                                } else {

                                                }
                                            }
                                        });
                            }
                        }
                        else
                        {
                            Toast.makeText(AddExercise.this,task.getException().toString(),Toast.LENGTH_SHORT);
                        }
                    }
                });
            //





                //AddExerciseToDbPlan(name);
              //  MoveBackToCreatePlanSecondStepExerciseAdded();

            }


        });

        addOwnExercise = findViewById(R.id.button_addOwnExercise_AddExercise);
      addOwnExercise.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MoveToAddYourOwnExercise();

            }
       });

    }



    private void MoveToAddYourOwnExercise()
    {
        Intent i = new Intent(this,AddYourOwnExercise.class);
        i.putExtra("slot",slot);
        startActivity(i);

    }

    private void MoveBackToCreatePlanSecondStepExerciseAdded()
    {
        Intent i = new Intent(this, CreateNewPlanSecondStep.class);
        i.putExtra("slot",slot);
        startActivity(i);
    }


    private void AddExerciseToDbPlan(final String exerciseName)
    {
        final CollectionReference colRef = db.collection("users").document(user.getUid()).collection("trainingPlans");

        colRef.whereEqualTo("slot",String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (final QueryDocumentSnapshot myDoc: task.getResult())
                    {



                        Map<String, Object> data = new HashMap<>();



                        data.put("name", exerciseName);
                        colRef.document(myDoc.getId()).collection("plan").add(data);




                    }
                }
                else
                {
                    Toast.makeText(AddExercise.this,task.getException().toString(),Toast.LENGTH_SHORT);
                }
            }
        });

    }


        private void Addex(String exName,String docTrainingId)
        {
           /* final CollectionReference colRef = db.collection("users").document(user.getUid()).collection("trainingPlans")
                    .document(docTrainingId).collection("plan");

            Map<String, Object> data = new HashMap<>();



            data.put("name", exName);
            colRef.add(data);




            CollectionReference myCol = colRef.document(myDoc.getId()).collection("plan");

            myCol.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        String docId = myDoc.getId();
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                boolean check = false;
                                for (QueryDocumentSnapshot document : task.getResult()) {


                                    if(document.getString("name").toLowerCase().equals(exerciseName.toLowerCase()))
                                    {

                                        check = true;
                                    }

                                }


                            } else {

                            }
                        }
                    });

            */
        }




    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,CreateNewPlanSecondStep.class);
        i.putExtra("slot",slot);
        startActivity(i);
    }
}
