package com.ethicnology.symbionte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.ethicnology.symbionte.FirebaseUtils.createFlatshare
import com.ethicnology.symbionte.FirebaseUtils.getCurrentUser
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
        getCurrentUser {
            it.flatshareId?.let { it1 -> updateUI(it1) }
        }
    }

    private fun updateUI(flatshareId: String){
        findViewById<TextView>(R.id.textViewFlatshareId).text = flatshareId
    }

    fun buttonCreateFlatshare(view: View){
        val flatshareName = findViewById<EditText>(R.id.editTextFlatshareName).text.toString()
        auth = Firebase.auth
        val authId = auth.currentUser?.uid
        val newFlatshare = authId?.let { Flatshare(flatshareName, it) }
        newFlatshare?.let { createFlatshare(it) }
        getCurrentUser {
            it.flatshareId?.let { it1 -> updateUI(it1) }
        }
    }

    fun buttonJoinFlatshare(view: View){
        val flatshareId = findViewById<EditText>(R.id.editTextFlatshareName).text.toString()
        //remove whitespace due to copy pasta
        val idClean = flatshareId.replace("\\s".toRegex(), "")
        auth = Firebase.auth
        val authId = auth.currentUser?.uid
        authId?.let {joinFlatshare(this, idClean)}
        getCurrentUser {
            it.flatshareId?.let { it1 -> updateUI(it1) }
        }
    }

}