package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddYourOwnExercise extends AppCompatActivity {

    private Button done;
    private EditText exerciseName;
    private TextView redHintName;
    private boolean ok = false;
    private int slot;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_own_exercise);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey("slot")) {
                slot = bundle.getInt("slot");
            }

        }


        done = findViewById(R.id.button_Done_AddYourOwnExercise);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ok) {
                    String exName = exerciseName.getText().toString().trim();
                    AddExerciseToDbUserExercises(exName);
                    AddExerciseToDbTrainingPlan(exName);
                    MoveToCreateNewPlanSecondStep();
                }


            }
        });

        exerciseName = findViewById(R.id.editText_ExerciseName_AddYourOwnExercise);
        redHintName = findViewById(R.id.textView_redhintname_AddYourOwnEzercise);
        redHintName.setText(R.string.addYourOwnExercise_wrong_name);

        exerciseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!CheckName(s.toString())) {
                    exerciseName.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
                    ok = false;
                    redHintName.setVisibility(View.VISIBLE);
                } else {
                    exerciseName.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
                    ok = true;
                    redHintName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AddExercise.class);
        intent.putExtra("slot", slot);
        startActivity(intent);

    }

    private void MoveToCreateNewPlanSecondStep() {

        Intent intent = new Intent(this, CreateNewPlanSecondStep.class);
        intent.putExtra("slot", slot);
        startActivity(intent);


    }


    private boolean CheckName(String name) {
        if (name.length() <= 0 || name.length() > 20) {
            return false;
        } else if (name.matches((".*[,./;'\\\\\\[\\]\\{\\}_+\\-=!@#$%^&*()~`<>?|\":;'].*")) || name.matches("\\s")) {
            return false;
        }

        return true;
    }

    private void AddExerciseToDbUserExercises(String exerciseName) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", exerciseName);
        db.collection("users").document(user.getUid()).collection("userExercises").add(data);
    }

    private void AddExerciseToDbTrainingPlan(final String exerciseName) {


        db.collection("users").document(user.getUid()).collection("trainingPlans").whereEqualTo("slot", String.valueOf(slot)).limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("name", exerciseName);
                            for (QueryDocumentSnapshot doc : task.getResult()) {

                                db.collection("users").document(user.getUid()).collection("trainingPlans").document(doc.getId()).collection("plan")
                                        .add(data);

                            }
                        }
                    }
                });
    }


}
