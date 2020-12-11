package com.ethicnology.symbionte.calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.CallBackMethods;
import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.Model.Event_model;
import com.ethicnology.symbionte.R;
import com.ethicnology.symbionte.TodoList.Add_Todo;
import com.ethicnology.symbionte.TodoList.Todo_List;
import com.ethicnology.symbionte.adapter.ListMemberAdapter;
import com.ethicnology.symbionte.adapter.ListTodoAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Add_event extends AppCompatActivity {
    private Intent received_intent;

    private CollectionReference ref;
    private FirebaseUser current_user_id;
    private FirebaseFirestore db;

    private EditText name_event;
    private TimePickerDialog time_picker;
    private EditText eText;
    private DatePickerDialog date_picker;
    private EditText eDate;

    private Button add_button;
    private Button cancel_button;
    private Switch all_day_switch;

    private RecyclerView members_list;
    private RecyclerView.LayoutManager layoutManager;
    private ListMemberAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        received_intent = getIntent();
        db = FirebaseFirestore.getInstance();
        ref = db.collection("colocations");
        add_button = findViewById(R.id.add_event_button);
        cancel_button = findViewById(R.id.cancel_event_button);
        name_event = findViewById(R.id.event_name);
        all_day_switch = findViewById(R.id.all_day_event);
        eText= findViewById(R.id.event_time);
        eDate = findViewById(R.id.event_date);

        current_user_id = FirebaseAuth.getInstance().getCurrentUser();
        members_list = findViewById(R.id.members_list);
        members_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        members_list.setLayoutManager(layoutManager);

        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);

                // time picker dialog
                time_picker = new TimePickerDialog(Add_event.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String check_time = time_formatter(sHour,sMinute);
                                //System.out.println(check_time);
                                eText.setText(check_time);
                            }
                        }, hour, minutes, true);
                time_picker.show();
            }
        });

        eDate.setInputType(InputType.TYPE_NULL);
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                date_picker = new DatePickerDialog(Add_event.this,R.style.MySpinnerDatePickerStyle ,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                date_picker.show();
            }
        });

        cancel();

        DataManager.getInstance().setFlatshareId(current_user_id.getUid(), new CallBackMethods() {
            @Override
            public void callback(final String flatshareId) {
                //System.out.println(flatshareId);
                final List<String>[] list_id_users = new List[]{new ArrayList<String>()};
                ref.document(flatshareId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        list_id_users[0] = (List<String>) task.getResult().get("members");
                        final List<String> list_users = new ArrayList<String>();
                        for (String user : list_id_users[0]){
                            db.collection("users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    list_users.add((String) documentSnapshot.get("first"));
                                    adapter = new ListMemberAdapter(Add_event.this,list_users);
                                    members_list.setAdapter(adapter);
                                }
                            });
                        }
                    }
                });
                //System.out.println(eDate.getText().toString()+ " Dehors");

                db.collection("users").document(current_user_id.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                        add_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                add_event(flatshareId,eDate.getText().toString(),name_event.getText().toString(),eText.getText().toString(),(String)task.getResult().get("first"),DataManager.getInstance().getMembers_selected());
                            }
                        });
                    }
                });
            }

        });

        all_day_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    System.out.println("Switch button on, so no time");
                    eText.setText(null);
                    eText.setEnabled(false);
                }
                else {
                    eText.setEnabled(true);
                }
            }
        });


    }

    public String time_formatter(int sHour, int sMin){
        String hour = sHour + "";
        String min = sMin + "";
        if (sHour < 10){
            hour = "0" + sHour;
        }
        if (sMin < 10){
            min = "0" + sMin;
        }
        return hour + ":" + min;
    }

    //Cancel button function
    public void cancel(){
        //System.out.println(cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_event.this, com.ethicnology.symbionte.calendar.Calendar.class);
                startActivity(intent);
            }
        });
    }

    //Add button function
    public void  add_event(final String flatshareId,String date, String name, String time, String createdBy, ArrayList<String> members){
        final Event_model event = new Event_model(date,name,time,createdBy,members);
        CollectionReference ref1 = ref.document(flatshareId).collection("Events");
        ref1.add(event).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Success", "New event");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("failure", "Can't add new event");

            }
        });
        Intent intent = new Intent(Add_event.this, com.ethicnology.symbionte.calendar.Calendar.class);
        startActivity(intent);
    }


}
