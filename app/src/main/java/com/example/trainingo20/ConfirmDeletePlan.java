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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ConfirmDeletePlan extends AppCompatActivity {

    private  int slot;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private Button buttonYes;
    private Button buttonNo;
    private TextView tvQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delete_plan);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        buttonNo = findViewById(R.id.button_No_ConfirmDeletePlan);
        buttonYes = findViewById(R.id.button_Yes_ConfirmDeletePlan);
        tvQuestion = findViewById(R.id.textView_Question_ConfirmDeletePlan);



        Bundle bundle = getIntent().getExtras();
        if(!bundle.isEmpty())
        {
            slot = bundle.getInt("slot");

        }



        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletePlan(slot);
                MoveToMyPlans_Deleted(slot);
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveToMyPlans();
            }
        });






    }


    private void DeletePlan(int slot)
    {

       final CollectionReference trainingPlansRef = db.collection("users").document(user.getUid()).collection("trainingPlans");


        trainingPlansRef.whereEqualTo("slot", String.valueOf(slot)).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot myDocument : task.getResult()) {
                        //now use id of document

                            trainingPlansRef.document(myDocument.getId())
                                    .update("name", FieldValue.delete(), "about", FieldValue.delete(), "status", "empty");

                            String idDoc = myDocument.getId();
                            trainingPlansRef.document(idDoc).collection("plan")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    document.getReference().delete();
                                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                                }
                                            } else {
                                                // Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });


                            //  Log.d(TAG, document.getId() + " => " + document.getData());

                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }




    }});}


    private void MoveToMyPlans()
    {
        Intent i = new Intent(ConfirmDeletePlan.this,MyPlans.class);
        startActivity(i);
    }

    private void MoveToMyPlans_Deleted(int slot)
    {
        Intent i = new Intent(ConfirmDeletePlan.this,MyPlans.class);
        i.putExtra("deletedPlanSlot",slot);
        startActivity(i);
    }



}
