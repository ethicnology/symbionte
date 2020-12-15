package com.ethicnology.symbionte.Expenses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.CallBackMethods;
import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.Model.Refund;
import com.ethicnology.symbionte.R;
import com.ethicnology.symbionte.TodoList.Todo_List;
import com.ethicnology.symbionte.adapter.ListMemberAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Add_Refund extends Activity {
    Intent expensesIntent = getIntent();
    FirebaseFirestore db;
    private Button add;
    private Button cancel;
    FirebaseUser current_user_id;
    private EditText amount_refund;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_refund);
        db = FirebaseFirestore.getInstance();
        current_user_id = FirebaseAuth.getInstance().getCurrentUser();
        final TextView amount_refund_user = findViewById(R.id.amount_refund_user);
        add = findViewById(R.id.add_Refund_button);
        amount_refund = findViewById(R.id.Refund_amout_add);
        cancel = findViewById(R.id.cancel_Refund_button);
        DataManager.getInstance().setFlatshareId(current_user_id.getUid(), new CallBackMethods() {
            @Override
            public void callback(final String flatshareId) {
                db.collection("users").document(current_user_id.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final String user = documentSnapshot.getString("first");
                        if (getIntent().getExtras() != null){
                            Float refund_amount = (float) getIntent().getIntExtra("amount_bill", 0) / getIntent().getIntExtra("nb_members",0);
                            amount_refund_user.setText(refund_amount.toString() + " €");
                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                                    Refund refund = new Refund(user,date,amount_refund.getText().toString());

                                        add_refund(user,flatshareId, getIntent().getStringExtra("bill_id"),refund);
                                    }

                                });
                        }
                    }
                });
            }
        });
    cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Add_Refund.this, Expenses.class);
            startActivity(intent);
        }
    });
    }




    public void add_refund(String user, String flatshareId, String bill_id, Refund refund){
        db.collection("colocations")
                .document(flatshareId)
                .collection("Expenses")
                .document(bill_id)
                .collection("Refund")
                .add(refund)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(Add_Refund.this, Expenses.class);
                        startActivity(intent);
                    }
                });
    }
}
