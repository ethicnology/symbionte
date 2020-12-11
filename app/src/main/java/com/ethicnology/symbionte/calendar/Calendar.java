package com.ethicnology.symbionte.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.CallBackMethods;
import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.Model.Event_model;
import com.ethicnology.symbionte.R;
import com.ethicnology.symbionte.adapter.ListEventAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Calendar extends AppCompatActivity {
    private CalendarView calendar;
    private TextView selectedDate;
    private RecyclerView events_list;
    private FloatingActionButton floatingActionButton;

    List<Event_model> event_list_display = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private ListEventAdapter adapter;

    private FirebaseFirestore db;
    private FirebaseUser current_user_id;
    private CollectionReference ref;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        current_user_id = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        ref = db.collection("colocations");

        events_list = findViewById(R.id.day_events);
        events_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        events_list.setLayoutManager(layoutManager);

        floatingActionButton = findViewById(R.id.add_event);
        addEvent();

        calendar = findViewById(R.id.calendarView);
        selectedDate = findViewById(R.id.selected_date);

        //To automatically select the current date
        setCurrentDate();
        DataManager.getInstance().setFlatshareId(current_user_id.getUid(), new CallBackMethods() {
            @Override
            public void callback(final String flatshareId) {
                load_data(flatshareId);
                //Get the selected date into the TextView called selected_date whenever the user pick a date
                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        month += 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        selectedDate.setText(date);
                        load_data(flatshareId);
                    }
                });
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast.makeText(Calendar.this, String.format("Current User %s ", currentUser), Toast.LENGTH_SHORT).show();
        getUser(currentUser.getUid());
    }

    public void getUser(String user_id) {
        DocumentReference user = db.collection("users").document(user_id);
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Firebase", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("Firebase", "No such document");
                    }
                } else {
                    Log.d("Firebase", "get failed with ", task.getException());
                }
            }
        });
    }

    public void setCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("d/MM/yyyy");
        selectedDate.setText(formatter.format(date));
    }

    //Floating button function - load add_event view
    public void addEvent() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, Add_event.class);
                startActivity(intent);
            }
        });
    }

    private void load_data(String flatshareId) {
        if (event_list_display.size() > 0) {
            event_list_display.clear();
        }
        ref.document(flatshareId)
                .collection("Events")
                .whereEqualTo("date", selectedDate.getText())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Event_model event = new Event_model(doc.getString("date"), doc.getString("name"), doc.getString("time"), doc.getString("created_by"), (ArrayList<java.lang.String>) doc.get("members"), doc.getId());
                            event_list_display.add(event);
                        }
                        adapter = new ListEventAdapter(Calendar.this, event_list_display);
                        events_list.setAdapter(adapter);
                    }
                });

    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        DataManager.getInstance().setFlatshareId(current_user_id.getUid(), new CallBackMethods() {
            @Override
            public void callback(String flatshareId) {
                if (item.getTitle().equals("Delete")) {
                    deleteEvent(event_list_display.get(item.getOrder()),flatshareId);
                }
            }
        });
        return super.onContextItemSelected(item);
    }



    private void deleteEvent(Event_model event, final String flatshareId){
        CollectionReference ref1 = ref.document(flatshareId).collection("Events");
        ref1.document(event.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        load_data(flatshareId);
                    }
                });
    }
}
