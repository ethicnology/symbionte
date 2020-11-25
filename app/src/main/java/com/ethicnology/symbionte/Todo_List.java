package com.ethicnology.symbionte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.Model.Todo;
import com.ethicnology.symbionte.adapter.ListItemAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Todo_List extends AppCompatActivity {

    List<Todo> todoList = new ArrayList<>();
    FirebaseFirestore db;

    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;
    Spinner dropdown;
    Button add_data;

    FloatingActionButton fab;


    ListItemAdapter adapter;

    protected void onCreate (Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.todo_list);

        db = FirebaseFirestore.getInstance();

        listItem = (RecyclerView)findViewById(R.id.listTodo);
        listItem.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);

        fab = (FloatingActionButton)findViewById(R.id.add_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.add_todo);
            }
        });

        //Get data from another layout.


        dropdown = findViewById(R.id.categories);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = (String)parent.getItemAtPosition(pos);
                loadData(item);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }





    private void loadData(String category) {
        if (todoList.size() > 0)
            todoList.clear();
        db.collection("ToDoList")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc:task.getResult()){
                            Todo todo = new Todo(
                                    doc.getString("id"),
                                    doc.getString("title"),
                                    doc.getString("description"),
                                    doc.getString("category"));
                            todoList.add(todo);
                        }
                        adapter = new ListItemAdapter(Todo_List.this,todoList);
                        listItem.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(Todo_List.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
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

        db.collection("TodoList").document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loadData(category);
            }
        });
    }
}
