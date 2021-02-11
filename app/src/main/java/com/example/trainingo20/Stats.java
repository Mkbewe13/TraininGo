package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class Stats extends AppCompatActivity {


    private ListView exerciseList;
    private EditText searchExercises;
    ArrayList<String> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        searchExercises = findViewById(R.id.editText_searchExercises_Stats);
        exerciseList = findViewById(R.id.listView_usedExercises_Stats);



        exercises = new ArrayList<String>();
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
                            ExerciseStatsAdapter adapter = new ExerciseStatsAdapter(Stats.this,R.layout.row_exercises_stats,exercises);
                            exerciseList.setAdapter(adapter);
                        } else {
                            Toast.makeText(Stats.this,task.getException().toString(),Toast.LENGTH_LONG).show();

                        }
                    }
                });



    searchExercises.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            FirebaseFirestore.getInstance().collection("exercisesGlobal")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Toast.makeText(Stats.this,"Success",Toast.LENGTH_LONG).show();

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    if(document.getString("name").matches("."+searchExercises.getText().toString()+"."))
                                    {
                                        String tmp = document.get("name").toString();
                                        exercises.add(tmp.substring(0, 1).toUpperCase() + tmp.substring(1).toLowerCase());
                                    }

                                }
                                ExerciseStatsAdapter adapter = new ExerciseStatsAdapter(Stats.this,R.layout.row_exercises_stats,exercises);
                                exerciseList.setAdapter(adapter);
                            } else {
                                Toast.makeText(Stats.this,task.getException().toString(),Toast.LENGTH_LONG).show();

                            }
                        }
                    });

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });






    }
}
