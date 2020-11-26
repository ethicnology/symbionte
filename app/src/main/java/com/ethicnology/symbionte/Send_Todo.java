package com.ethicnology.symbionte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.Model.Todo;
import com.ethicnology.symbionte.adapter.ListItemAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Send_Todo  extends AppCompatActivity {

    Intent todo_list_Intent = getIntent();
    List<Todo> todoList = new ArrayList<>();
    FirebaseFirestore db;

    Spinner spinner_category;
    String selected_item_add;
    Button add;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo);
        db = FirebaseFirestore.getInstance();
        add = (Button)findViewById(R.id.send_data);
        spinner_category = findViewById(R.id.spinner_category);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = (String)parent.getItemAtPosition(pos);
                selected_item_add = item;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout title_input = findViewById(R.id.textInput_title);
                TextInputLayout description_input = findViewById(R.id.textInput_description);
                setData(title_input.getEditText().getText().toString(),description_input.getEditText().getText().toString(), selected_item_add);

                Intent intent = new Intent(Send_Todo.this,Todo_List.class);
                startActivity(intent);
            }
        });
    }

    private void setData(String title, String description, final String category){
        String id = UUID.randomUUID().toString();
        Map<String,Object> todo = new HashMap<>();
        todo.put("id",id);
        todo.put("title",title);
        todo.put("description",description);
        todo.put("category", category);

        db.collection("ToDoList").document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }
}
