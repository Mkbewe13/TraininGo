package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DeleteExerciseFromCreatorList extends AppCompatActivity {


    private Button buttonYes, buttonNo;
    private TextView textViewQuestion;
    private int slot;
    private String exerciseToDelete;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_exercise_from_creator_list);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        buttonYes = findViewById(R.id.button_Yes_DeleteExerciseFromCreatorList);
        buttonNo = findViewById(R.id.button_No_DeleteExerciseFromCreatorList);
        textViewQuestion = findViewById(R.id.textView_Question_DeleteExerciseFromCreatorList);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("deleteData")) {
                String[] deleteData = bundle.getStringArray("deleteData");
                slot = Integer.parseInt(deleteData[0]);
                exerciseToDelete = deleteData[1];

            }
        }

        textViewQuestion.setText("Do you want to remove " + exerciseToDelete + " from your training plan?");


        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToCreatePlanSecondStep();
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExercise();
                MoveToCreatePlanSecondStep();

            }
        });


    }

    @Override
    public void onBackPressed() {
    }

    private void MoveToCreatePlanSecondStep() {
        Intent i = new Intent(this, CreateNewPlanSecondStep.class);
        i.putExtra("slot", slot);
        startActivity(i);
    }

    private void AddExercise() {
        final CollectionReference colRef = db.collection("users").document(user.getUid()).collection("trainingPlans");

        colRef.whereEqualTo("slot", String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot myDoc : task.getResult()) {


                        Map<String, Object> data = new HashMap<>();
                        data.put("name", exerciseToDelete);

                        colRef.document(myDoc.getId()).collection("plan").add(data);

                    }
                } else {
                    Toast.makeText(DeleteExerciseFromCreatorList.this, task.getException().toString(), Toast.LENGTH_SHORT);
                }
            }
        });


    }


}
