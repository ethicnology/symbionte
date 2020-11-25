package com.ethicnology.symbionte.adapter;


import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.Model.Todo;
import com.ethicnology.symbionte.R;
import com.ethicnology.symbionte.Todo_List;

import java.util.List;

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{



    ItemClickListener itemClickListener;
    TextView item_title,item_description;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        item_title = (TextView)itemView.findViewById(R.id.item_title);
        item_description = (TextView)itemView.findViewById(R.id.item_description);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(),"Modify");
        contextMenu.add(0,0,getAdapterPosition(),"Done");
        contextMenu.add(0,0,getAdapterPosition(),"Delete");
    }
}


public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder>{

    Todo_List todo_list_activity;
    List<Todo> todoList;



    public ListItemAdapter(Todo_List todo_list_activity, List<Todo> todoList) {
        this.todo_list_activity = todo_list_activity;
        this.todoList = todoList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(todo_list_activity.getBaseContext());
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        holder.item_title.setText(todoList.get(position).getTitle());
        holder.item_description.setText(todoList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
