package com.example.trainingo20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyPlans extends AppCompatActivity {

    private Button firstSlot, secondSlot, thirdSlot, fourthSlot;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plans);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        SetButtons();


        //DeleteTmpPlans();

        firstSlot = findViewById(R.id.button_plan1_MyPlans);
        secondSlot = findViewById(R.id.button_plan2_MyPlans);
        thirdSlot = findViewById(R.id.button_plan3_MyPlans);
        fourthSlot = findViewById(R.id.button_plan4_MyPlans);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("dataStep3"))   //resolve getting new plan's name to slow problem
            {
                String[] dataArray;
                dataArray = bundle.getStringArray("dataStep3");
                SetNewName(dataArray[0], dataArray[1]);
            }
            if(bundle.containsKey("deletedPlanSlot"))
            {
                SetNewName(String.valueOf(bundle.getInt("slot")), "+");
            }
        }


        firstSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckIfSlotIsEmpty(1);
            }
        });

        secondSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckIfSlotIsEmpty(2);
            }
        });

        thirdSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckIfSlotIsEmpty(3);
            }
        });


        fourthSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckIfSlotIsEmpty(4);
            }
        });


        firstSlot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                MoveToConfirmDeletePlan(1);
                return true;
            }
        });
        secondSlot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                MoveToConfirmDeletePlan(2);
                return true;
            }
        });
        thirdSlot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                MoveToConfirmDeletePlan(3);
                return true;
            }
        });
        fourthSlot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                MoveToConfirmDeletePlan(4);
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainMenu.class);
        startActivity(i);
    }

    private void MoveToCreatePlan(int slot) {
        Intent i = new Intent(this, CreateNewPlan.class);
        i.putExtra("slot", slot);
        startActivity(i);

    }
    private void MoveToEditPlan(int slot) {
        Intent i = new Intent(this, EditPlanFirstStep.class);
        i.putExtra("slot", slot);
        startActivity(i);

    }
    private void MoveToConfirmDeletePlan(int slot)
    {



        Intent i  = new Intent(this,ConfirmDeletePlan.class);
        i.putExtra("slot",slot);
        startActivity(i);

    }

    private void CheckIfSlotIsEmpty(final int slot) {





        db.collection("users").document(user.getUid()).collection("trainingPlans").whereEqualTo("slot", String.valueOf(slot)).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot qds : task.getResult()) {
                        if (qds.getString("status").equals("taken")) {
                            Toast.makeText(MyPlans.this, "taken", Toast.LENGTH_SHORT).show();
                            MoveToEditPlan(slot);

                        } else {
                            Toast.makeText(MyPlans.this, "empty", Toast.LENGTH_SHORT).show();
                            MoveToCreatePlan(slot);
                            }
                        }
                    }

                }


            });
    }
    private void SetNewName(String slot, String name)  // set new plan's name after creating or editing
    {
        switch (slot) {
            case "1":
                firstSlot.setText(name);
                break;
            case "2":
                secondSlot.setText(name);
                break;
            case "3":
                thirdSlot.setText(name);
                break;
            case "4":
                fourthSlot.setText(name);
                break;
        }
    }

    private void SetButtons()  // set buttons display   "+" if slot is empty or "plan's name" when slot is taken
    {
        db.collection("users").document(user.getUid()).collection("trainingPlans").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot qds : task.getResult()) {
                        switch (qds.getString("slot")) {
                            case "1":
                                switch (qds.getString("status")) {
                                    case "empty":
                                        firstSlot.setText("+");
                                        break;
                                    case "taken":
                                        firstSlot.setText(qds.getString("name"));
                                        break;
                                }
                                break;

                            case "2":
                                switch (qds.getString("status")) {
                                    case "empty":
                                        secondSlot.setText("+");
                                        break;
                                    case "taken":
                                        secondSlot.setText(qds.getString("name"));
                                        break;
                                }
                                break;
                            case "3":
                                switch (qds.getString("status")) {
                                    case "empty":
                                        thirdSlot.setText("+");
                                        break;
                                    case "taken":
                                        thirdSlot.setText(qds.getString("name"));
                                        break;
                                }
                                break;
                            case "4":
                                switch (qds.getString("status")) {
                                    case "empty":
                                        fourthSlot.setText("+");
                                        break;
                                    case "taken":
                                        fourthSlot.setText(qds.getString("name"));
                                        break;
                                }
                                break;


                        }
                    }
                }
            }
        });

    }

    //delete all plans where status is tmp, tmp means changes are not saved. Not done yet.
    private void DeleteTmpPlans() {

        final CollectionReference colTrainingPlans = db.collection("users").document(user.getUid()).collection("trainingPlans");

        colTrainingPlans
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot myDocument : task.getResult()) {
                        //now use id of document
                        if (myDocument.getString("status").equals("tmp")) {
                            colTrainingPlans.document(myDocument.getId())
                                    .update("name", FieldValue.delete(), "about", FieldValue.delete(), "status", "empty");

                            String idDoc = myDocument.getId();
                            colTrainingPlans.document(idDoc).collection("plan")
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
                    }
                } else {
                    // Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


}
