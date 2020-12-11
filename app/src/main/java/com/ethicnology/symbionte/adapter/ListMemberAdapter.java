package com.ethicnology.symbionte.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.R;

import java.util.List;

public class ListMemberAdapter extends RecyclerView.Adapter<ListMemberViewHolder> {
    Activity activity;
    List<String> members_list;

    public ListMemberAdapter(Activity activity, List<String> members_list) {
        this.activity = activity;
        this.members_list = members_list;
    }

    @Override
    public ListMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
        View view = inflater.inflate(R.layout.list_member_single, parent,false);
        return new ListMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListMemberViewHolder holder, int position) {
        holder.checkBox.setText(members_list.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataManager.getInstance().add_member((String) holder.checkBox.getText());
                }
                else {
                    DataManager.getInstance().delete_member((String) holder.checkBox.getText());
                }
                System.out.println(holder.checkBox.getText());
            }
        });
    }

    @Override
    public int getItemCount() {
        return members_list.size();
    }
}

class ListMemberViewHolder extends RecyclerView.ViewHolder {

    CheckBox checkBox;

    public ListMemberViewHolder(View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.checkBox);
    }
}
