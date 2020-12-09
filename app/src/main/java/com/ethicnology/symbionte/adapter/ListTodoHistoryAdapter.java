package com.ethicnology.symbionte.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.Model.Todo;
import com.ethicnology.symbionte.R;

import java.util.List;


public class ListTodoHistoryAdapter extends RecyclerView.Adapter<ListTodoHistoryViewHolder>{

    List<Todo> todoList;
    AppCompatActivity activity;


    public ListTodoHistoryAdapter(AppCompatActivity activity, List<Todo> todoList) {
        this.activity = activity;
        this.todoList = todoList;
    }

    @Override
    public ListTodoHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ListTodoHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTodoHistoryViewHolder holder, int position) {
        holder.item_title.setText(todoList.get(position).getTitle());
        holder.item_title.setTextColor(Color.parseColor("#ffffff"));

        holder.item_description.setText(todoList.get(position).getDescription());
        holder.item_description.setTextColor(Color.parseColor("#ffffff"));

        holder.item_user_first.setText("For : " + todoList.get(position).getUser_first());
        holder.item_user_first.setTextColor(Color.parseColor("#d5d5d5"));

        if (todoList.get(position).isDeleted()){
            holder.itemView.setBackgroundColor(Color.argb(255, 187, 11, 11));
        }
        else {
            holder.itemView.setBackgroundColor(Color.argb(255, 52, 201, 36));
        }
    }



    @Override
    public int getItemCount() {
        return todoList.size();
    }
}

class ListTodoHistoryViewHolder extends RecyclerView.ViewHolder{

    TextView item_title,item_description,item_user_first;

    public ListTodoHistoryViewHolder(View itemView) {
        super(itemView);
        item_title = (TextView)itemView.findViewById(R.id.item_title);
        item_description = (TextView)itemView.findViewById(R.id.item_description);
        item_user_first = (TextView)itemView.findViewById(R.id.item_user_first);
    }
}