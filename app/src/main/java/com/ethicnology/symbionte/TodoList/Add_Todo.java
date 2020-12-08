package com.ethicnology.symbionte.TodoList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ethicnology.symbionte.DataManager;
import com.ethicnology.symbionte.Model.Todo;
import com.ethicnology.symbionte.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class Add_Todo extends AppCompatActivity {

    Intent todo_list_Intent = getIntent();
    FirebaseFirestore db;

    Spinner spinner_category;
    Spinner spinner_user;
    String selected_category_add;
    String selected_user_first_add;
    Button add;
    CollectionReference ref;
    FirebaseUser current_user_auth;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo);
        db = FirebaseFirestore.getInstance();
        current_user_auth = FirebaseAuth.getInstance().getCurrentUser();

        ref = db.collection("colocations");

        add = (Button)findViewById(R.id.send_data);
        spinner_category = findViewById(R.id.spinner_category);
        spinner_user = findViewById(R.id.dropdown_user);
        setFlatshareId(current_user_auth.getUid(), new CallBackMethods() {
            @Override
            public void callback(final String flatshareId) {
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
                                    ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(Add_Todo.this, android.R.layout.simple_spinner_item, list_users);
                                    usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner_user.setAdapter(usersAdapter);
                                }
                            });
                        }



                        spinner_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selected_user_first_add = list_users.get(pos);
                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

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
                        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(Add_Todo.this, android.R.layout.simple_spinner_item, list_category);
                        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_category.setAdapter(addressAdapter);



                        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                selected_category_add = list_category_id.get(pos);
                            }
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final TextInputLayout title_input = findViewById(R.id.textInput_title);
                                final TextInputLayout description_input = findViewById(R.id.textInput_description);
                                setData(title_input.getEditText().getText().toString(),description_input.getEditText().getText().toString(),selected_user_first_add,flatshareId, selected_category_add);
                                Intent intent = new Intent(Add_Todo.this,Todo_List.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });



    }

    private void setData(String title, String description, String user_first,String flatshareId, String category_id){
        String id = UUID.randomUUID().toString();
        Map<String,Object> todo = new HashMap<>();
        todo.put("id",id);
        todo.put("title",title);
        todo.put("description",description);
        todo.put("user_first", user_first);
        CollectionReference ref1 = ref.document(flatshareId).collection("ToDoList");
        ref1.document(category_id)
                .collection("tasks")
                .document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Add_Todo.this,"task has been upload", Toast.LENGTH_LONG).show();
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
