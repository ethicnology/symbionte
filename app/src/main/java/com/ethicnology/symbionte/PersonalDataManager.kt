package com.ethicnology.symbionte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import com.ethicnology.symbionte.FirebaseUtils.getCurrentUser
import com.ethicnology.symbionte.FirebaseUtils.setUser

class PersonalDataManager : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_data_manager)
        getCurrentUser{
            updateUI(it)
        }
    }

    private fun updateUI(user: User){
            findViewById<EditText>(R.id.editTextFirstName).text = Editable.Factory.getInstance().newEditable(user.first)
            findViewById<EditText>(R.id.editTextLastName).text = Editable.Factory.getInstance().newEditable(user.last)
    }

    fun buttonUpdate(view: View){
        val firstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
        val lastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
        getCurrentUser {
            it.first = firstName
            it.last = lastName
            Log.w("TAH", it.toString())
            setUser(it)
        }
    }
}