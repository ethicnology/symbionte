package com.ethicnology.symbionte.TodoList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ethicnology.symbionte.CallBackMethods;
import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.Model.Todo;
import com.ethicnology.symbionte.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Update_Todo extends AppCompatActivity {
    FirebaseFirestore db;

    Button update;
    CollectionReference ref;
    FirebaseUser current_user_auth;
    TextInputEditText input_title;
    TextInputEditText input_description;
    Spinner spinner_user;
    String selected_user_first;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_todo);
        db = FirebaseFirestore.getInstance();
        current_user_auth = FirebaseAuth.getInstance().getCurrentUser();

        ref = db.collection("colocations");
        input_title = findViewById(R.id.input_title_update);
        input_description = findViewById(R.id.input_description_update);
        update = findViewById(R.id.update);
        spinner_user = findViewById(R.id.dropdown_user);

        DataManager.getInstance().setFlatshareId(current_user_auth.getUid(), new CallBackMethods() {
            @Override
            public void callback(final String flatshareId) {
                final List<String>[] list_id_users = new List[]{new ArrayList<String>()};
                ref.document(flatshareId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        list_id_users[0] = (List<String>) task.getResult().get("members");
                        final List<String> list_users = new ArrayList<String>();
                        for (String user : list_id_users[0]) {
                            db.collection("users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    list_users.add((String) documentSnapshot.get("first"));
                                    ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(Update_Todo.this, android.R.layout.simple_spinner_item, list_users);
                                    usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_user.setAdapter(usersAdapter);
                                }
                            });
                        }


                        spinner_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selected_user_first = list_users.get(pos);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                selected_user_first = list_users.get(0);
                            }
                        });

                    }

                });


                if (getIntent().getExtras() != null) {

                    final Todo todo = getIntent().getParcelableExtra("Todo");
                    input_title.setHint(todo.getTitle());
                    input_description.setHint(todo.getDescription());
                    DataManager.getInstance().setFlatshareId(current_user_auth.getUid(), new CallBackMethods() {
                        @Override
                        public void callback(final String flatshareId) {
                            ref.document(flatshareId).collection("ToDoList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    update.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (!input_description.getText().toString().matches("")) {
                                                todo.setDescription(input_description.getText().toString());
                                            }
                                            if (!input_title.getText().toString().matches("")) {
                                                todo.setTitle(input_title.getText().toString());
                                            }
                                            todo.setUser_first(selected_user_first);
                                            modifyItem(flatshareId, DataManager.getInstance().getCategory_selected(), todo);
                                            Intent intent = new Intent(Update_Todo.this, Todo_List.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void modifyItem(final String flatshareId, final String category_id, Todo todo) {
        CollectionReference ref1 = ref.document(flatshareId).collection("ToDoList");
        ref1.document(category_id)
                .collection("tasks")
                .document(todo.getId())
                .set(todo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Succès");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                System.out.println("Echec");
            }
        });

    }
}
