package com.ethicnology.symbionte.Expenses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.CallBackMethods;
import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.Model.Bill;
import com.ethicnology.symbionte.Model.Event_model;
import com.ethicnology.symbionte.R;
import com.ethicnology.symbionte.adapter.ListMemberAdapter;
import com.ethicnology.symbionte.calendar.Add_event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Add_Bill extends AppCompatActivity {
    Intent expensesIntent = getIntent();
    FirebaseFirestore db;
    private Button add;
    private Button cancel;
    FirebaseUser current_user_id;
    private EditText name_Bill;
    private EditText amount_Bill;
    private RecyclerView members_list;
    private RecyclerView.LayoutManager layoutManager;
    private ListMemberAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bill);
        db = FirebaseFirestore.getInstance();
        current_user_id = FirebaseAuth.getInstance().getCurrentUser();
        members_list = findViewById(R.id.members_list_bill);

        members_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        members_list.setLayoutManager(layoutManager);
        add = findViewById(R.id.add_bill_button);
        cancel = findViewById(R.id.cancel_bill_button);
        name_Bill = findViewById(R.id.bill_name_add);
        amount_Bill = findViewById(R.id.bill_amout_add);
        DataManager.getInstance().setFlatshareId(current_user_id.getUid(), new CallBackMethods() {
            @Override
            public void callback(final String flatshareId) {
                final List<String>[] list_id_users = new List[]{new ArrayList<String>()};
                db.collection("colocations").document(flatshareId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        list_id_users[0] = (List<String>) task.getResult().get("members");
                        final List<String> list_users = new ArrayList<String>();
                        for (String user : list_id_users[0]){
                            db.collection("users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    list_users.add((String) documentSnapshot.get("first"));
                                    adapter = new ListMemberAdapter(Add_Bill.this,list_users);
                                    members_list.setAdapter(adapter);
                                }
                            });
                        }
                    }
                });
                db.collection("users").document(current_user_id.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                                add_bill(flatshareId,date,name_Bill.getText().toString(),Integer.valueOf(amount_Bill.getText().toString()),(String)task.getResult().get("first"),DataManager.getInstance().getMembers_selected());
                            }
                        });
                    }
                });
            }

        });

    }

    public void  add_bill(final String flatshareId,String date, String name, int amount, String createdBy, ArrayList<String> members){
        final Bill bill = new Bill(name, createdBy,date,amount,members);
        db.collection("colocations")
                .document(flatshareId).collection("Expenses")
                .add(bill).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Success", "New bill");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("failure", "Can't add new bill");

            }
        });
        Intent intent = new Intent(Add_Bill.this, Expenses.class);
        startActivity(intent);
    }

}
