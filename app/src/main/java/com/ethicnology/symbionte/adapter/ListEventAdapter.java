package com.ethicnology.symbionte.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.Calendar;
import com.ethicnology.symbionte.Event_model;
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
        holder.event_name.setText(eventList.get(position).getName());
        holder.event_date.setText(eventList.get(position).getDate());
    }



    @Override
    public int getItemCount() {
        return eventList.size();
    }
}

class ListEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    TextView event_date, event_name;

    public ListEventViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        event_date = (TextView)itemView.findViewById(R.id.event_date_single);
        event_name = (TextView)itemView.findViewById(R.id.event_name_single);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(),"example");
    }
}

