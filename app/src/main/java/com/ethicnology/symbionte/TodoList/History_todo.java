package com.ethicnology.symbionte.TodoList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.CallBackMethods;
import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.Model.Todo;
import com.ethicnology.symbionte.R;
import com.ethicnology.symbionte.adapter.ListTodoAdapter;
import com.ethicnology.symbionte.adapter.ListTodoHistoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class History_todo extends AppCompatActivity {
    Spinner dropdown;
    FirebaseFirestore db;
    CollectionReference ref;
    FirebaseUser current_user_auth;
    List<Todo> todoList = new ArrayList<>();
    ListTodoHistoryAdapter adapter;
    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_todo);
        dropdown = findViewById(R.id.categories);
        db = FirebaseFirestore.getInstance();
        ref = db.collection("colocations");
        listItem = (RecyclerView)findViewById(R.id.listTodo);
        listItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);
        current_user_auth = FirebaseAuth.getInstance().getCurrentUser();

        DataManager.getInstance().setFlatshareId(current_user_auth.getUid(), new CallBackMethods() {
            @Override
            public void callback(final String flatshareId) {

                ref.document(flatshareId).collection("ToDoList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final List<String> list_category = new ArrayList<String>();
                        final List<String> list_category_id = new ArrayList<String>();
                        for (DocumentSnapshot doc:task.getResult()){
                            String category = doc.getString("category");
                            String id_category = doc.getId();
                            list_category.add(category);
                            list_category_id.add(id_category);
                        }
                        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(History_todo.this, android.R.layout.simple_spinner_item, list_category);
                        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dropdown.setAdapter(addressAdapter);



                        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                loadDataHistory(list_category_id.get(pos), flatshareId);
                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
                });
            }
        });


    }

    private void loadDataHistory(String category, String flatshareId) {
        if (todoList.size() > 0)
            todoList.clear();
        CollectionReference ref1 = ref.document(flatshareId).collection("ToDoList");
        ref1.document(category).collection("history_tasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc:task.getResult()){
                    Todo todo = new Todo(
                            doc.getId(),
                            doc.getString("title"),
                            doc.getString("description"),
                            doc.getString("user_first"),
                            doc.getBoolean("deleted")
                    );
                    todoList.add(todo);
                }
                adapter = new ListTodoHistoryAdapter(History_todo.this,todoList);
                listItem.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(History_todo.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
