package com.ethicnology.symbionte

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

object FirebaseUtils {
    private val db = Firebase.firestore

    fun getUser(UID: String): User? {
        var result: User? = null
        val docRef = db.collection("users").document(UID)
        docRef.get().addOnSuccessListener { document ->
            val user = document.toObject<User>()
            Log.d("FirebaseUtils", "data: $user")
            //TODO find a way to return
        }
        return result
    }

    fun getFlatshare(flatshareId: String){
        val docRef = db.collection("colocations").document(flatshareId)
        docRef.get().addOnSuccessListener { document ->
            val flatshare = document.toObject<Flatshare>()
            Log.d("FirebaseUtils", "data: $flatshare")
            //TODO find a way to return
        }
    }
}