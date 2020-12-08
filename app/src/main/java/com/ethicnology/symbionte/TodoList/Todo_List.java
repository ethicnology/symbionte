package com.ethicnology.symbionte.TodoList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.Model.Todo;
import com.ethicnology.symbionte.R;
import com.ethicnology.symbionte.adapter.ListItemAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class Todo_List extends AppCompatActivity {

    List<Todo> todoList = new ArrayList<>();


    FirebaseFirestore db;
    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;
    Spinner dropdown;
    FloatingActionButton fab;
    ListItemAdapter adapter;
    CollectionReference ref;
    FirebaseUser current_user_auth;

    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list);
        db = FirebaseFirestore.getInstance();
        ref = db.collection("colocations");

        current_user_auth = FirebaseAuth.getInstance().getCurrentUser();



        listItem = (RecyclerView)findViewById(R.id.listTodo);
        listItem.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);

        fab = (FloatingActionButton)findViewById(R.id.add_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Todo_List.this, Add_Todo.class);
                startActivity(intent);
            }
        });




        dropdown = findViewById(R.id.categories);
        setFlatshareId(current_user_auth.getUid(), new CallBackMethods() {
            @Override
            public void callback(final String flatshareId) {

                ref.document(flatshareId).collection("members").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc:task.getResult()){
                            System.out.println("ID : "+doc.getId());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("ECHEC");
                    }
                });


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
                        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(Todo_List.this, android.R.layout.simple_spinner_item, list_category);
                        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dropdown.setAdapter(addressAdapter);



                        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                //selected_item_filter = (String)parent.getItemAtPosition(pos);
                                DataManager.getInstance().setCategory_selected(list_category_id.get(pos));
                                loadData(list_category_id.get(pos), flatshareId);
                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
                });
            }
        });




    }

    private void loadData(String category, String flatshareId) {
        if (todoList.size() > 0)
            todoList.clear();
        CollectionReference ref1 = ref.document(flatshareId).collection("ToDoList");
        ref1.document(category).collection("tasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc:task.getResult()){
                    Todo todo = new Todo(
                            doc.getId(),
                            doc.getString("title"),
                            doc.getString("description"),
                            doc.getString("user_first")
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
    }



    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        setFlatshareId(current_user_auth.getUid(), new CallBackMethods() {
            @Override
            public void callback(String flatshareId) {
                if (item.getTitle().equals("Delete") && DataManager.getInstance().getCategory_selected() != null){
                    deleteItem(item.getOrder(),flatshareId,DataManager.getInstance().getCategory_selected());
                }
                else if (item.getTitle().equals("Modify") && DataManager.getInstance().getCategory_selected() != null){
                    Intent intent = new Intent(Todo_List.this,Update_Todo.class);
                    intent.putExtra("Todo", (Parcelable) todoList.get(item.getOrder()));
                    startActivity(intent);
                }
                else {
                    System.out.println("null");
                }


            }
        });

        return super.onContextItemSelected(item);
    }

    private void deleteItem(int index, final String flatshareId, final String category_id) {
        CollectionReference ref1 = ref.document(flatshareId).collection("ToDoList");
        ref1.document(category_id)
                .collection("tasks")
                .document(todoList.get(index).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData(category_id, flatshareId);
                    }
                });
    }



    public void setFlatshareId(String UID, final CallBackMethods callBackMethods){

        DocumentReference docRef = db.collection("users").document(UID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                String result = doc.getString("flatshareId");
                callBackMethods.callback(result);
            }
        });
    }

    interface CallBackMethods{
        void callback(String flatshareId);
    }




}
