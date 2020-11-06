package com.ethicnology.symbionte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Flatshare : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    val TAG = "Firestore"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flatshare)
    }
    override fun onStart(){
        super.onStart()
        auth = Firebase.auth
        val authUser = auth.currentUser
        updateUI(authUser)
    }

    fun updateUI(user: FirebaseUser?){
        val user = user?.uid?.let { db.collection("users").document(it) }
        user?.get()?.addOnSuccessListener { document ->
            if (document.data?.get("flatshareId") != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                // Replace placeholders by users values
                findViewById<TextView>(R.id.textViewFlatshareId).text = Editable.Factory.getInstance().newEditable(document.data?.get("flatshareId").toString())
            } else {
                Log.d(TAG, "User doesn't have any Flatshare")
            }
        }?.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
    }

    fun createFlatshare(authUser: FirebaseUser){
        val FlatshareName = findViewById<EditText>(R.id.editTextFlatshareName).text.toString()
        if (authUser != null) {
            // Add a new Flatshare document
            db.collection("colocations")
                .add(hashMapOf(
                    "name" to FlatshareName,
                    "admin" to authUser.uid,
                ))
                .addOnSuccessListener {
                    documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    // Add the created Flatshare Id to user document
                    db.collection("users").document(authUser.uid).update("flatshareId", documentReference.id)
                    Log.d("Firestore", "updateUser ${authUser.uid} :success")
                    updateUI(authUser)
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
        }
    }

    fun buttonCreateFlatshare(view: View){
        auth = Firebase.auth
        val authUser = auth.currentUser
        if (authUser != null) {
            createFlatshare(authUser)
        }
    }

    private fun joinFlatshare(authUser: FirebaseUser, FlatshareId: String){
        db.collection("colocations").document(FlatshareId)
            .update("members", FieldValue.arrayUnion(authUser.uid))
            .addOnSuccessListener {
                Log.d(TAG, "{$FlatshareId successfully updated!")
                authUser.uid?.let {
                    db.collection("users").document(authUser.uid).update("flatshareId", FlatshareId)
                    Log.d("Firestore", "updateUser ${authUser.uid} :success")
                    updateUI(authUser)
                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating $FlatshareId", e) }
    }

    fun buttonJoinFlatshare(view: View){
        val FlatshareId = findViewById<EditText>(R.id.editTextFlatshareName).text.toString()
        auth = Firebase.auth
        val authUser = auth.currentUser
        if (authUser != null) {
            joinFlatshare(authUser, FlatshareId)
        }
    }
}