package com.ethicnology.symbionte.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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


public class ListBillAdapter extends RecyclerView.Adapter<ListBillViewHolder>{

    List<Bill> billList;
    Expenses activity;


    public ListBillAdapter(Expenses activity, List<Bill> billList) {
        this.activity = activity;
        this.billList = billList;
    }

    @Override
    public ListBillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
        View view = inflater.inflate(R.layout.list_bills,parent,false);
        return new ListBillViewHolder(view, billList, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ListBillViewHolder holder, final int position) {
        holder.bill_amount.setText(billList.get(position).getAmount() + " €");
        holder.bill_created_by.setText("Created by : " + billList.get(position).getCreated_by());
        holder.bill_date.setText(billList.get(position).getDate());
        holder.bill_name.setText(billList.get(position).getName());
        holder.bill_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().setFlatshareId(FirebaseAuth.getInstance().getUid(), new CallBackMethods() {
                    @Override
                    public void callback(final String flatshareId) {
                        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                activity.deleteBill(billList.get(position),flatshareId,documentSnapshot.getString("first"));
                            }
                        });


                    }
                });
            }
        });
        holder.bill_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startRefundActivity(billList.get(position).getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return billList.size();
    }
}






class ListBillViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView bill_name,bill_date,bill_created_by,bill_amount;
    ImageButton bill_delete, bill_refund;
    List<Bill> billList;
    Expenses activity;
    ArrayList<String> members_list;


    public ListBillViewHolder(View itemView, List<Bill> billList, Expenses activity) {
        super(itemView);
        itemView.setOnClickListener(this);
        bill_name = (TextView)itemView.findViewById(R.id.bill_name);
        bill_date = (TextView)itemView.findViewById(R.id.bill_date);
        bill_created_by = (TextView)itemView.findViewById(R.id.bill_created_by);
        bill_amount = (TextView)itemView.findViewById(R.id.bill_amount);
        bill_delete = (ImageButton) itemView.findViewById(R.id.bill_delete);
        bill_refund = (ImageButton) itemView.findViewById(R.id.bill_refund);
        this.billList = billList;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Bill current_bill = this.billList.get(getAdapterPosition());
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final AlertDialog alert = builder.create();
        members_list = current_bill.getMembers();

        View view = LayoutInflater.from(activity).inflate(R.layout.popup_bill, null);
        TextView name, date, created_by, amount;
        ListView listview;
        Button close;
        close = view.findViewById(R.id.close_popup_bill);
        name = view.findViewById(R.id.name_bill_popup);
        date = view.findViewById(R.id.date_bill_popup);
        created_by = view.findViewById(R.id.created_by_bill_popup);
        amount = view.findViewById(R.id.amount_bill_popup);
        listview = (ListView)view.findViewById(R.id.list_view_member_bill_popup);
        amount.setText(current_bill.getAmount() + " €");

        name.setText(current_bill.getName());
        date.setText(current_bill.getDate());
        created_by.setText("Created by " + current_bill.getCreated_by());
        final ListBillViewHolder.StableArrayAdapter adapter = new ListBillViewHolder.StableArrayAdapter(activity,android.R.layout.simple_list_item_1,members_list);
        listview.setAdapter(adapter);

        RecyclerView listItem = (RecyclerView)view.findViewById(R.id.listRefund);
        listItem.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        listItem.setLayoutManager(layoutManager);
        ListRefundAdapter adapterRefund = new ListRefundAdapter(activity,current_bill.getRefunds());
        listItem.setAdapter(adapterRefund);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

        alert.setView(view);
        alert.show();
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}