package com.ethicnology.symbionte.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.calendar.Calendar;
import com.ethicnology.symbionte.Model.Event_model;
import com.ethicnology.symbionte.R;

import java.util.List;

public class ListEventAdapter extends RecyclerView.Adapter<ListEventViewHolder> {

    Calendar calendar_activity;
    List<Event_model> eventList;

    public ListEventAdapter(Calendar calendar_activity, List<Event_model> eventList) {
        this.calendar_activity = calendar_activity;
        this.eventList = eventList;
    }

    @Override
    public ListEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(calendar_activity.getBaseContext());
        View view = inflater.inflate(R.layout.list_event_single,parent,false);
        return new ListEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListEventViewHolder holder, int position) {
        if (eventList.get(position).getTime() != null){
            holder.event_time.setText(eventList.get(position).getTime());
        }
        holder.event_name.setText(eventList.get(position).getName());
        System.out.println(eventList.get(position).getCreated_by());
        holder.event_created_by.setText("created by " + eventList.get(position).getCreated_by());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}

class ListEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    TextView event_time, event_name, event_created_by;

    public ListEventViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        event_time = itemView.findViewById(R.id.event_time_single);
        event_name = itemView.findViewById(R.id.event_name_single);
        event_created_by = itemView.findViewById(R.id.event_created_by);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //contextMenu.setHeaderTitle("Click to delete");
        contextMenu.add(0,0,getAdapterPosition(),"Delete");
    }
}

