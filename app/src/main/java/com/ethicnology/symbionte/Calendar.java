package com.ethicnology.symbionte;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.adapter.ListEventAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseFirestore db;

    private ListEventAdapter adapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        db = FirebaseFirestore.getInstance();

        events_list = (RecyclerView)findViewById(R.id.day_events);
        events_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        events_list.setLayoutManager(layoutManager);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.add_event);
        addEvent();

        calendar = (CalendarView) findViewById(R.id.calendarView);
        selectedDate = (TextView) findViewById(R.id.selected_date);

        //To automatically select the current date
        setSelectedDate();
        load_data();

        //Get the selected date into the TextView called selected_date whenever the user pick a date
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                selectedDate.setText(date);
                load_data();
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

    public void getUser(String user_id){
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

    public void getFlatshare(String flatshare_id){
        DocumentReference flatshare = db.collection("colocations").document(flatshare_id);
        flatshare.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    public void setSelectedDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        selectedDate.setText(formatter.format(date));
    }

    //Floating button function - load add_event view
    public void addEvent(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendar.this, Add_event.class);
                intent.putExtra("date",selectedDate.getText());
                startActivity(intent);
            }
        });
    }

    private void load_data(){
        if(event_list_display.size()>0){
            event_list_display.clear();
        }
        db.collection("events")
                .whereEqualTo("date",selectedDate.getText())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc:task.getResult()){
                            Event_model event = new Event_model(
                                    doc.getString("date"),
                                    doc.getString("name"));
                            event_list_display.add(event);
                        }
                        adapter = new ListEventAdapter(Calendar.this,event_list_display);
                        events_list.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Calendar.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
