package com.example.trainingo20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class AddExercise extends AppCompatActivity {

    private ListView exercisesList;
    private ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        exercisesList = (ListView) findViewById(R.id.listView_exercises_AddExercise);

        String exercises[] = {"Bench press", "Pull up", "Squat", "Biceps curl","Bench press", "Pull up", "Squat", "Biceps curl","Bench press", "Pull up", "Squat", "Biceps curl"};   //MAX 18 znaków

        ArrayList<String> trainings = new ArrayList<String>();
        trainings.addAll( Arrays.asList(exercises) );

        adapter = new ArrayAdapter<String>(this, R.layout.row_add_exercise, trainings);

        exercisesList.setAdapter(adapter);

        exercisesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String) parent.getItemAtPosition(position);


            }


        });

    }
}
