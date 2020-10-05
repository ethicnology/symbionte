package com.ethicnology.symbionte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonalDataManager : AppCompatActivity() {
    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore
    private val TAG = "Firestore"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_data_manager)
    }

    fun buttonAdd(view: View){
        addAlanTuring()
    }

    private fun addAlanTuring() {
        // [START add_ada_lovelace]
        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to "Alan",
            "last" to "Turing",
            "born" to 1912
        )
        // Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        Toast.makeText(this, "User added to firestore", Toast.LENGTH_SHORT).show()
        // [END add_ada_lovelace]
    }

    private fun getAllUsers() {
        // [START get_all_users]
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
        // [END get_all_users]
    }


}