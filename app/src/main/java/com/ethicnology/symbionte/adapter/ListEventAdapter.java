package com.ethicnology.symbionte.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.calendar.Add_event;
import com.ethicnology.symbionte.calendar.Calendar;
import com.ethicnology.symbionte.Model.Event_model;
import com.ethicnology.symbionte.R;

import java.util.ArrayList;
import java.util.HashMap;
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
        return new ListEventViewHolder(view, eventList, calendar_activity);
    }

    @Override
    public void onBindViewHolder(ListEventViewHolder holder, int position) {
        if (eventList.get(position).getTime() != null){
            holder.event_time.setText(eventList.get(position).getTime());
        }
        holder.event_name.setText(eventList.get(position).getName());
        holder.event_created_by.setText("created by " + eventList.get(position).getCreated_by());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


}

class ListEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    TextView event_time, event_name, event_created_by;
    List<Event_model> eventList;
    Calendar calendar;
    ArrayList<String> members_list;

    public ListEventViewHolder(View itemView, List<Event_model> eventList, Calendar calendar) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        event_time = itemView.findViewById(R.id.event_time_single);
        event_name = itemView.findViewById(R.id.event_name_single);
        event_created_by = itemView.findViewById(R.id.event_created_by);
        this.eventList = eventList;
        this.calendar = calendar;
    }

    @Override
    public void onClick(View v) {
        Event_model current_event = this.eventList.get(getAdapterPosition());
        AlertDialog.Builder builder = new AlertDialog.Builder(calendar);
        AlertDialog alert = builder.create();
        members_list = current_event.getMembers();

        View view = LayoutInflater.from(calendar).inflate(R.layout.popup_event, null);
        TextView time, name, date, created_by;
        ListView listview;
        time = view.findViewById(R.id.time);
        name = view.findViewById(R.id.name);
        date = view.findViewById(R.id.date);
        created_by = view.findViewById(R.id.created_by);
        listview = (ListView)view.findViewById(R.id.list_view_member);

        if (current_event.getTime() != ""){
            time.setText("at : "+current_event.getTime());
        }
        name.setText(current_event.getName());
        date.setText(current_event.getDate());
        created_by.setText("Created by " + current_event.getCreated_by());
        final StableArrayAdapter adapter = new StableArrayAdapter(calendar,android.R.layout.simple_list_item_1,members_list);
        listview.setAdapter(adapter);
        System.out.println(members_list + "after adapter");

        alert.setView(view);
        alert.show();
    }


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //contextMenu.setHeaderTitle("Click to delete");
        contextMenu.add(0,0,getAdapterPosition(),"Delete");
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


