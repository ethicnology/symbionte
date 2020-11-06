package com.ethicnology.symbionte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonalDataManager : AppCompatActivity() {
    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore
    private val TAG = "Firestore"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_data_manager)
        auth = Firebase.auth
        // Get authentified user
        val authUser = auth.currentUser
        // Get user document by user auth uid
        val user = authUser?.uid?.let { db.collection("users").document(it) }
        user?.get()?.addOnSuccessListener { document ->
            if (document != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                // Replace placeholders by users values
                findViewById<EditText>(R.id.editTextFirstName).text = Editable.Factory.getInstance().newEditable(document.data?.get("first").toString())
                findViewById<EditText>(R.id.editTextLastName).text = Editable.Factory.getInstance().newEditable(document.data?.get("last").toString())
            } else {
                Log.d(TAG, "No such document")
            }
        }?.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }

    }

    private fun updateUser(){
        // Get inputs values
        val firstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
        val lastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
        val authUser = auth.currentUser
        // Update user document
        val user = authUser?.uid?.let { db.collection("users").document(it) }
        user?.update(mapOf(
            "first" to firstName,
            "last" to lastName,
        ))?.addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            ?.addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    fun buttonUpdate(view: View){
        updateUser()
    }
}