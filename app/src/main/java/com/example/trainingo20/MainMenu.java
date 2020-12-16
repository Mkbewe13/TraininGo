package com.example.trainingo20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {


    Button go;
    Button myPlans;
    Button stats;
    Button profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        myPlans = findViewById(R.id.button_myplans_MainMenu);

myPlans.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     MoveToMyPlans();
    }
});
    }


    private void MoveToMyPlans()
    {
        Intent i = new Intent(this,MyPlans.class);
        startActivity(i);

    }

    private void MoveToStats()
    {

    }

    private void MoveToProfile()
    {

    }

    private void MoveToGo()
    {

    }





}
