package com.ethicnology.symbionte;

import androidx.annotation.NonNull;

import com.ethicnology.symbionte.Model.Todo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager sInstance;


    private String category_selected = null;



    private Todo todo = null;


    // private constructor to limit new instance creation
    private DataManager() {
        // may be empty
    }

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }
    public String getCategory_selected() {
        return category_selected;
    }

    public void setCategory_selected(String category_selected) {
        this.category_selected = category_selected;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }



}