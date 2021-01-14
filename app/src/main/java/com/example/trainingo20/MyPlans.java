package com.example.trainingo20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyPlans extends AppCompatActivity {

    Button firstSlot,secondSlot,thirdSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plans);

        firstSlot = findViewById(R.id.button_plan1_MyPlans);
        secondSlot = findViewById(R.id.button_plan2_MyPlans);
        thirdSlot = findViewById(R.id.button_plan3_MyPlans);


        firstSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToCreatePlan();
            }
        });

    }


    private void MoveToCreatePlan()
    {
        Intent i = new Intent(this,CreateNewPlan.class);
        startActivity(i);

    }
}
