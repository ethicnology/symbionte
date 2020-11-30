package com.ethicnology.symbionte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.ethicnology.symbionte.FirebaseUtils.setUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Authentication : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication)
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?){
        user?.let{
            findViewById<TextView>(R.id.textViewUserId).text = user.uid
        } ?: run {
            findViewById<TextView>(R.id.textViewUserId).text = "null"
        }
    }

    fun buttonSignUp(view: View){
        val email = findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.editTextPassword).text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AUTH", "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    // Create a user document for his/her personal data
                    val newUser = user?.let { User(it.uid) }
                    newUser?.let { setUser(it) }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AUTH", "createUserWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    fun buttonLogIn(view: View){
        // Get inputs values
        val email = findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
        // Try to sign in using Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AUTH", "logInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AUTH", "logInWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
    }
}