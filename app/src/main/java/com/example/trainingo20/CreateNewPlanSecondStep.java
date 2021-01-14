package com.example.trainingo20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateNewPlanSecondStep extends AppCompatActivity {

    private Button nextStep;
    private ListView exercisesList;
    private ArrayAdapter<String> adapter;
    private  Button addExercise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_plan_second_step);

        exercisesList = (ListView) findViewById(R.id.listView_exercises_CreateNewPlanSecondStep);

        String exercises[] = {"Bench press", "Pull up", "Squat", "Biceps curl"};   //MAX 18 znaków

        ArrayList<String> trainings = new ArrayList<String>();
        trainings.addAll( Arrays.asList(exercises) );

        adapter = new ArrayAdapter<String>(this, R.layout.row_choose_training, trainings);

        exercisesList.setAdapter(adapter);

       /* exercisesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);
                MoveToConfirmTraining(name);

            }

                    onclick na obiekt z listy
        }); */


       addExercise = findViewById(R.id.button_add_CreateNewPlanSecondStep);
       addExercise.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               MoveToAddExercises();
           }
       });

    }


    private void MoveToAddExercises()
    {
        Intent i = new Intent(this,AddExercise.class);
        startActivity(i);
    }
}

