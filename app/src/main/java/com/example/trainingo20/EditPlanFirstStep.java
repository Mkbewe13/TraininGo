package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class EditPlanFirstStep extends AppCompatActivity {

    private EditText editTextName,editTextAbout;
    private Button buttonNext,buttonCancel;
    private TextView nameRedHint, aboutRedHint;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
private int slot;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan_first_step);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        editTextAbout = findViewById(R.id.editText_about_EditPlanFirstStep);
        editTextName = findViewById(R.id.editText_planname_EditPlanFirstStep);

        buttonCancel  = findViewById(R.id.button_cancel_EditPlanFirstStep);
        buttonNext = findViewById(R.id.button_next_EditPlanFirstStep);

        nameRedHint = findViewById(R.id.textView_redhintname_EditPlanFirstStep);
        aboutRedHint = findViewById(R.id.textView_redhintabout_EditPlanFirstStep);


        Bundle extras = getIntent().getExtras();
            if(!extras.isEmpty())
            {
                if(extras.containsKey("slot"))
                {
                    slot = extras.getInt("slot");
                }
            }


            db.collection("users").document(user.getUid()).collection("trainingPlans").whereEqualTo("slot",String.valueOf(slot)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(QueryDocumentSnapshot qds : task.getResult())
                        {

                            editTextName.setText(qds.getString("name"));
                            editTextAbout.setText(qds.getString("about"));
                        }
                    }
                }
            });


            //// Onchangelistener and validatiom

            //// Onclick listener save and cancel


    }


}
