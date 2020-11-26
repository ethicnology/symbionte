package com.ethicnology.symbionte

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Flatshare(ID: String) {
    val ID: String = ID
    var admin: String? = null
    var members: Array<String>? = null
    var name: String? = null

    val db = Firebase.firestore

    fun fetchData(){
        val docRef = db.collection("users").document(this.ID)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("FLATSHARE", "Document : ${document.data}")
                    this.admin = document.get("admin") as String?
                    this.members = document.get("members") as Array<String>?
                    this.name = document.get("name") as String?
                } else {
                    Log.d("FLATSHARE", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("FLATSHARE", "get failed with ", exception)
            }
    }
}