package com.example.trainingo20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateNewPlan extends AppCompatActivity {


    Button nextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_plan);

        nextStep = findViewById(R.id.button_next_CreateNewPlan);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToNextStep();
            }
        });
    }



   private void MoveToNextStep()
    {
        Intent i = new Intent(this,CreateNewPlanSecondStep.class);
        startActivity(i);
    }
}
