package com.ethicnology.symbionte

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ethicnology.symbionte.FirebaseUtils.getCurrentUser
import com.ethicnology.symbionte.FirebaseUtils.setUser
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint


class PersonalDataManager : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personal_data_manager)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentUser{
            updateUI(it)
        }
    }

    private fun updateUI(user: User){
        findViewById<EditText>(R.id.editTextFirstName).text = Editable.Factory.getInstance().newEditable(user.first)
        findViewById<EditText>(R.id.editTextLastName).text = Editable.Factory.getInstance().newEditable(user.last)
        if(user.location != null){
            findViewById<EditText>(R.id.editTextLatitude).text = Editable.Factory.getInstance().newEditable(user.location!!.latitude.toString())
            findViewById<EditText>(R.id.editTextLongitude).text = Editable.Factory.getInstance().newEditable(user.location!!.longitude.toString())
        }
    }

    @SuppressLint("MissingPermission")
    fun buttonLocateMe(view: View){
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    getCurrentUser {
                        if(location != null){
                            it.location = GeoPoint(location.latitude,location.longitude)
                            updateUI(it)
                        }
                    }
                }
    }

    fun buttonUpdate(view: View){
        val firstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
        val lastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
        val latitude = findViewById<EditText>(R.id.editTextLatitude).text.toString()
        val longitude = findViewById<EditText>(R.id.editTextLongitude).text.toString()
        getCurrentUser {
            it.first = firstName
            it.last = lastName
            if(latitude.isNotBlank() && longitude.isNotBlank()){
                it.location = GeoPoint(latitude.toDouble(), longitude.toDouble())
            }
            setUser(it)
            Toast.makeText(this, "Personal data updated", Toast.LENGTH_SHORT).show()
            val gotoIncipit = Intent(this, Incipit::class.java)
            startActivity(gotoIncipit)
        }
    }

}