package com.ethicnology.symbionte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.ethicnology.symbionte.FirebaseUtils.createFlatshare
import com.ethicnology.symbionte.FirebaseUtils.joinFlatshare
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class FlatshareManager : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flatshare_manager)
    }

    override fun onStart(){
        super.onStart()
        auth = Firebase.auth
        val authId = auth.currentUser?.uid
    }

    fun buttonCreateFlatshare(view: View){
        val flatshareName = findViewById<EditText>(R.id.editTextFlatshareName2).text.toString()
        auth = Firebase.auth
        val authId = auth.currentUser?.uid
        val newFlatshare = authId?.let { Flatshare(flatshareName, it) }
        newFlatshare?.let { createFlatshare(it) }
        Toast.makeText(this, "Flatshare created", Toast.LENGTH_SHORT).show()
        val gotoIncipit = Intent(this, Incipit::class.java).apply {putExtra(EXTRA_MESSAGE, data)}
        startActivity(gotoIncipit)
    }

    fun buttonJoinFlatshare(view: View){
        val flatshareId = findViewById<EditText>(R.id.editTextFlatshareID).text.toString()
        //remove whitespace due to copy pasta
        val idClean = flatshareId.replace("\\s".toRegex(), "")
        auth = Firebase.auth
        val authId = auth.currentUser?.uid
        authId?.let {joinFlatshare(this, idClean)}
        Toast.makeText(this, "Flatshare joined", Toast.LENGTH_SHORT).show()
        val gotoIncipit = Intent(this, Incipit::class.java).apply {putExtra(EXTRA_MESSAGE, data)}
        startActivity(gotoIncipit)
    }

}