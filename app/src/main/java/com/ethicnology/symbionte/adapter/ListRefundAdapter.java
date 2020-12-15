package com.ethicnology.symbionte.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.CallBackMethods;
import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.Expenses.Add_Refund;
import com.ethicnology.symbionte.Expenses.Expenses;
import com.ethicnology.symbionte.Model.Bill;
import com.ethicnology.symbionte.Model.Event_model;
import com.ethicnology.symbionte.Model.Refund;
import com.ethicnology.symbionte.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListRefundAdapter extends RecyclerView.Adapter<ListRefundViewHolder>{

    List<Refund> refundList;
    Expenses activity;


    public ListRefundAdapter(Expenses activity, List<Refund> refundList) {
        this.activity = activity;
        this.refundList = refundList;
    }

    @Override
    public ListRefundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
        View view = inflater.inflate(R.layout.list_refunds,parent,false);
        return new ListRefundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRefundViewHolder holder, final int position) {
        holder.refund_amount.setText(refundList.get(position).getAmount() + " €");
        holder.refund_created_by.setText("Refund by : " + refundList.get(position).getCreated_by());
        holder.refund_date.setText(refundList.get(position).getDate());
    }


    @Override
    public int getItemCount() {
        return refundList.size();
    }
}






class ListRefundViewHolder extends RecyclerView.ViewHolder{

    TextView refund_date,refund_created_by,refund_amount;


    public ListRefundViewHolder(View itemView) {
        super(itemView);
        refund_date = (TextView)itemView.findViewById(R.id.refund_date);
        refund_created_by = (TextView)itemView.findViewById(R.id.refund_created_by);
        refund_amount = (TextView)itemView.findViewById(R.id.refund_amount);
    }

}