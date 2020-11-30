package com.ethicnology.symbionte

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

object FirebaseUtils {
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    fun getCurrentUser(myCallback: (User) -> Unit) {
        auth = Firebase.auth
        val currentUser = auth.currentUser
        currentUser?.let { it ->
            db.collection("users").document(it.uid).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result.toObject<User>()
                    user?.let { myCallback(it) }
                }
            }
        }
    }

    fun getCurrentFlatshare(myCallback: (Flatshare) -> Unit){
        getCurrentUser {
            it.flatshareId?.let { it1 ->
                db.collection("colocations").document(it1).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val flatshare = task.result.toObject<Flatshare>()
                        flatshare?.let { myCallback(it) }
                    }
                }
            }
        }
    }

    fun getUser(UID: String, myCallback: (User) -> Unit) {
        db.collection("users").document(UID).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result.toObject<User>()
                user?.let { myCallback(it) }
            }
        }
    }


}