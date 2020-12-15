package com.ethicnology.symbionte.Expenses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.CallBackMethods;
import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.Model.Bill;
import com.ethicnology.symbionte.Model.Event_model;
import com.ethicnology.symbionte.Model.Refund;
import com.ethicnology.symbionte.R;
import com.ethicnology.symbionte.TodoList.Add_Todo;
import com.ethicnology.symbionte.TodoList.Todo_List;
import com.ethicnology.symbionte.adapter.ListBillAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Expenses extends AppCompatActivity {

    List<Bill> billList = new ArrayList<>();

    FirebaseFirestore db;
    ListBillAdapter adapter;
    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses);
        db = FirebaseFirestore.getInstance();
        final FirebaseUser current_user_auth = FirebaseAuth.getInstance().getCurrentUser();

        listItem = (RecyclerView)findViewById(R.id.listBill);
        listItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);
        DataManager.getInstance().setFlatshareId(current_user_auth.getUid(), new CallBackMethods() {
            @Override
            public void callback(final String flatshareId) {
                loadBill(flatshareId);
                getTotalAmount(flatshareId);
                db.collection("users").document(current_user_auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        getCurrentUserAmount(flatshareId, documentSnapshot.getString("first"));

                    }
                });

            }
        });
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add_bill);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Expenses.this, Add_Bill.class);
                startActivity(intent);
            }
        });

    }


    private void loadBill(final String flatshareId) {
        if (billList.size() > 0)
            billList.clear();
        db.collection("colocations")
                .document(flatshareId)
                .collection("Expenses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc:task.getResult()){
                    final Bill bill = new Bill(
                            doc.getString("name"),
                            doc.getString("created_by"),
                            doc.getString("date"),
                            ((Long)doc.get("amount")).intValue(),
                            (ArrayList<String>) doc.get("members")
                    );
                    bill.setId(doc.getId());
                    db.collection("colocations")
                            .document(flatshareId)
                            .collection("Expenses")
                            .document(doc.getId()).collection("Refund").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Refund> list_refund = new ArrayList<>();
                            for (DocumentSnapshot doc:task.getResult()){
                                Refund refund = new Refund(doc.get("created_by").toString(),doc.get("date").toString(),doc.get("amount").toString());
                                refund.setId(doc.getId());
                                list_refund.add(refund);
                            }
                            bill.setRefunds(list_refund);
                        }
                    });

                    billList.add(bill);
                }
                adapter = new ListBillAdapter(Expenses.this,billList);
                listItem.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(Expenses.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getTotalAmount(String flatshareId){

        final TextView total_amount = findViewById(R.id.total_amount);
        total_amount.setText("0 €");
        db.collection("colocations")
                .document(flatshareId)
                .collection("Expenses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        int result = 0;
                        for (DocumentSnapshot doc:task.getResult()){
                            result = result + ((Long)doc.get("amount")).intValue();

                            total_amount.setText("Total : "+ String.valueOf(result) + " €");
                        }
                    }
                });
    }
    private void getCurrentUserAmount(String flatshareId, String userFirst){
        final TextView current_user_amount = findViewById(R.id.current_user_amount);
        current_user_amount.setText("0 €");
        db.collection("colocations")
                .document(flatshareId)
                .collection("Expenses")
                .whereArrayContains("members", userFirst)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        int result = 0;
                        for (DocumentSnapshot doc:task.getResult()){
                            result = result + ((Long)doc.get("amount")).intValue();
                            current_user_amount.setText("Mon total : "+ String.valueOf(result) + " €");
                        }
                        }
                    });
                }


    public void deleteBill(Bill bill, final String flatshareId, final String userFirst){
        db.collection("colocations")
                .document(flatshareId)
                .collection("Expenses")
                .document(bill.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadBill(flatshareId);
                        getTotalAmount(flatshareId);
                        getCurrentUserAmount(flatshareId,userFirst);
                    }
                });
    }


    public void startRefundActivity(String bill_id) {
        Intent intent = new Intent(Expenses.this, Add_Refund.class);
        intent.putExtra("bill_id", bill_id);
        startActivity(intent);
    }
}

