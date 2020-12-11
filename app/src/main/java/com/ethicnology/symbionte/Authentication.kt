package com.ethicnology.symbionte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.ethicnology.symbionte.FirebaseUtils.setUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Authentication : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication)
        auth = Firebase.auth
        val checkAuth = Firebase.auth.currentUser
        if (checkAuth != null){
            Toast.makeText(this, "Already authentified", Toast.LENGTH_SHORT).show()
            val gotoIncipit = Intent(this,Incipit::class.java).apply {putExtra(EXTRA_MESSAGE, data)}
            startActivity(gotoIncipit)
        }
    }

    public override fun onStart() {
        super.onStart()
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
        if(email.isNotBlank() && password.isNotBlank()){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        updateUI(user)
                        user?.let { setUser(User(it.uid)) }
                        Toast.makeText(this, "Sign up successfully", Toast.LENGTH_SHORT).show()
                        val gotoIncipit = Intent(this, Incipit::class.java)
                        startActivity(gotoIncipit)
                    } else {
                        Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        } else {
            Toast.makeText(this, "Please fill the form", Toast.LENGTH_SHORT).show()
        }


    }

    fun buttonLogIn(view: View){
        // Get inputs values
        val email = findViewById<EditText>(R.id.editTextEmailAddress).text.toString()
        val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
        if(email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Log in successfully", Toast.LENGTH_SHORT).show()
                        updateUI(user)
                        val gotoIncipit = Intent(this, Incipit::class.java)
                        startActivity(gotoIncipit)
                    } else {
                        Toast.makeText(this, "Log in failed", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        } else {
            Toast.makeText(this, "Please fill the form", Toast.LENGTH_SHORT).show()
        }
    }
}