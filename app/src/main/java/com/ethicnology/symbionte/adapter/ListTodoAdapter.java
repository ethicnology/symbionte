package com.ethicnology.symbionte.adapter;


import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.Model.Todo;
import com.ethicnology.symbionte.R;

import java.util.List;

class ListTodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{


    TextView item_title,item_description,item_user_first;

    public ListTodoViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        item_title = (TextView)itemView.findViewById(R.id.item_title);
        item_description = (TextView)itemView.findViewById(R.id.item_description);
        item_user_first = (TextView)itemView.findViewById(R.id.item_user_first);
    }


    @Override
    public void onClick(View v) {
        //itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(),"Modify");
        contextMenu.add(0,0,getAdapterPosition(),"Done");
        contextMenu.add(0,0,getAdapterPosition(),"Delete");
    }
}


public class ListTodoAdapter extends RecyclerView.Adapter<ListTodoViewHolder>{

    List<Todo> todoList;
    AppCompatActivity activity;


    public ListTodoAdapter(AppCompatActivity activity, List<Todo> todoList) {
        this.activity = activity;
        this.todoList = todoList;
    }

    @Override
    public ListTodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity.getBaseContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ListTodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListTodoViewHolder holder, int position) {
        holder.item_title.setText(todoList.get(position).getTitle());
        holder.item_description.setText(todoList.get(position).getDescription());
        holder.item_user_first.setText("For : " + todoList.get(position).getUser_first());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
