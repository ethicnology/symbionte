package com.ethicnology.symbionte

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.security.AccessController.getContext

object FirebaseUtils {
    private val db = Firebase.firestore
    lateinit var auth: FirebaseAuth
    private val TAG = "FirebaseUtils"

    fun setUser(user: User){
        user.id?.let {
            db.collection("users").document(it).set(user)
                .addOnSuccessListener { Log.d(TAG, "Document ${user.id} successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }

    fun getCurrentUser(myCallback: (User) -> Unit) {
        auth = Firebase.auth
        val currentUser = auth.currentUser
        currentUser?.let { it ->
            db.collection("users").document(it.uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result.toObject<User>()
                        user?.let { myCallback(it) }
                    }
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error with current user", e) }
        }
    }

    fun getUser(id: String, myCallback: (User) -> Unit) {
        db.collection("users").document(id).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result.toObject<User>()
                    user?.let { myCallback(it) }
                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error get user ${id}", e) }
    }

    fun createFlatshare(flatshare: Flatshare){
        db.collection("colocations")
            .add(flatshare)
            .addOnSuccessListener {
                //Create the Flatshare document
                document -> Log.d(TAG, "Document ${document.id} successfully written!")
                //Get the Firebase generated id
                flatshare.id = document.id
                //Update the id field with the document id
                setFlatshare(flatshare)
                //Append the admin user to members
                flatshare.admin?.let { appendFlatshareMember(flatshare, it) }
                //Update current user flatshareId
                getCurrentUser {
                    it.flatshareId = flatshare.id
                    setUser(it)
                }
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error create flatshare", e) }
    }

    fun joinFlatshare(context: Context, flatshareId: String){
        db.collection("colocations").document(flatshareId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val flatshare = task.result.toObject<Flatshare>()
                if (flatshare != null) {
                    getCurrentUser {
                        if(flatshare != null && it != null) {
                            it.flatshareId = flatshare?.id
                            setUser(it)
                            appendFlatshareMember(flatshare, it.id!!)
                        }
                    }
                    Toast.makeText(context, "$flatshareId Flatshare joined", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context, "Flatshare $flatshareId doesn't exist", Toast.LENGTH_LONG).show()
                }
            }
        }
        .addOnFailureListener { e -> Log.w(TAG, "Error join flatshare", e) }
    }

    fun setFlatshare(flatshare: Flatshare){
        flatshare.id?.let {
            db.collection("colocations").document(it)
                .set(flatshare)
                .addOnSuccessListener { Log.d(TAG, "Flatshare ${flatshare.id} successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating flatshare", e) }
        }
    }

    fun appendFlatshareMember(flatshare: Flatshare, userId: String){
        flatshare.id?.let {
            db.collection("colocations").document(it)
                .update("members", FieldValue.arrayUnion(userId))
                .addOnSuccessListener { Log.d(TAG, "Flatshare ${flatshare.id} successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating flatshare", e) }
        }
    }

    fun removeFlatshareMember(flatshare: Flatshare, userId: String){
        flatshare.id?.let {
            db.collection("colocations").document(it)
                .update("members", FieldValue.arrayRemove(userId))
                .addOnSuccessListener { Log.d(TAG, "Flatshare ${flatshare.id} successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating flatshare", e) }
        }
    }

    fun getCurrentFlatshare(myCallback: (Flatshare) -> Unit){
        getCurrentUser {
            it.flatshareId?.let { it1 ->
                db.collection("colocations").document(it1).get()
                    .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val flatshare = task.result.toObject<Flatshare>()
                        flatshare?.let { myCallback(it) }
                    }
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error with current flatshare", e) }
            }
        }
    }
}