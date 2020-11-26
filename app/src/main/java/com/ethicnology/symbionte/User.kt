package com.ethicnology.symbionte

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class User(UID: String) {
    val UID: String = UID
    var first: String? = null
    var last: String? = null
    var flatshareId: String? = null

    val db = Firebase.firestore

    fun getData(){
        val docRef = db.collection("users").document(this.UID)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("USER", "Document : ${document.data}")
                    this.first = document.get("first") as String?
                    this.last = document.get("last") as String?
                    this.flatshareId = document.get("flatshareId") as String?
                } else {
                    Log.d("USER", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("USER", "get failed with ", exception)
            }
    }
}