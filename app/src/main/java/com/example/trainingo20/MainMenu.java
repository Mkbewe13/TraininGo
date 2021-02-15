package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainMenu extends AppCompatActivity {


    private Button go;
    private Button myPlans;
    private Button stats;
    private Button profile;
    private TextView dailyMessage;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dailyMessage = findViewById(R.id.textView_daily_MainMenu);
        myPlans = findViewById(R.id.button_myplans_MainMenu);
        stats = findViewById(R.id.button_stats_MainMenu);
        go = findViewById(R.id.button_go_MainMenu);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        SetDailyMessage();

        myPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToMyPlans();
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToStats();
            }
        });

        profile = findViewById(R.id.button_profile_MainMenu);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToProfile();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMenu.this, "Not yet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }


    private void MoveToMyPlans() {
        Intent i = new Intent(this, MyPlans.class);
        startActivity(i);

    }

    private void MoveToStats() {
        Intent i = new Intent(this, Stats.class);
        startActivity(i);
    }

    private void MoveToProfile() {
        Intent i = new Intent(this, Profile.class);
        startActivity(i);
    }

    private void SetDailyMessage() {
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        dailyMessage.setText("Hello " + document.getString("name") + ". You lifted 1600kg this week");
                        dailyMessage.setVisibility(View.VISIBLE);

                        //Toast.makeText(MainMenu.this,document.getString("name"),Toast.LENGTH_LONG).show();
                    } else {
                        //  Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //tbc..
    }


}
