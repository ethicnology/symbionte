package com.ethicnology.symbionte;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
    FloatingActionButton fab;
    String selected_item_filter;
    ListItemAdapter adapter;
    CollectionReference ref;
    FirebaseUser current_user_auth;
    String flatshareId;


    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list);
        db = FirebaseFirestore.getInstance();
        ref = db.collection("colocations");

        current_user_auth = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(current_user_auth.getUid());
        User current_user = new User(current_user_auth.getUid());
        current_user.fetchData();
        System.out.println("Current flatshare: "+current_user.getFlatshareId());

        listItem = (RecyclerView)findViewById(R.id.listTodo);
        listItem.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);

        fab = (FloatingActionButton)findViewById(R.id.add_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Todo_List.this,Send_Todo.class);
                startActivity(intent);
            }
        });




        dropdown = findViewById(R.id.categories);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selected_item_filter = (String)parent.getItemAtPosition(pos);
                loadData(selected_item_filter);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }


    private void loadData(String category) {
        if (todoList.size() > 0)
            todoList.clear();
        CollectionReference ref1 = ref.document("Kle3FcAEFZIvn2Pvh36p").collection("ToDoList");
        ref1.document("0VToLTA80lcn3Yu6C7DJ").collection("tasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc:task.getResult()){
                    Todo todo = new Todo(
                            doc.getString("id"),
                            doc.getString("title"),
                            doc.getString("description")
                    );
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
        /*ref.document("Kle3FcAEFZIvn2Pvh36p").collection("ToDoList")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc:task.getResult()){
                            System.out.println(doc.getId());
                            Todo todo = new Todo(
                                    doc.getString("id"),
                                    doc.getString("title"),
                                    doc.getString("description")
                                    );
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
        });*/
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Delete"))
            deleteItem(item.getOrder());
        return super.onContextItemSelected(item);
    }

    private void deleteItem(int index) {
        db.collection("ToDoList")
                .document(todoList.get(index).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData(selected_item_filter);
                    }
                });
    }
}
