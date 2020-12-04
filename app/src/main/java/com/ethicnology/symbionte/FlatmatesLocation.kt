package com.ethicnology.symbionte

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import com.ethicnology.symbionte.PermissionUtils.requestPermission
import com.ethicnology.symbionte.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.ethicnology.symbionte.PermissionUtils.isPermissionGranted
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ethicnology.symbionte.FirebaseUtils.getCurrentFlatshare
import com.ethicnology.symbionte.FirebaseUtils.getCurrentUser
import com.ethicnology.symbionte.FirebaseUtils.getUser
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FlatmatesLocation : AppCompatActivity(),
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * [.onRequestPermissionsResult].
     */
    private var permissionDenied = false
    private lateinit var map: GoogleMap
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flatmates_location)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()
        getCurrentUser(){
            if(it.location != null){
                val position = LatLng(it.location!!.latitude, it.location!!.longitude)
                val zoom = 14.0f
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom))
                Toast.makeText(this, "Blue Marker represents your last shared location", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "You didn't shared any location to flatmates yet", Toast.LENGTH_LONG).show()
            }
        }

        auth = Firebase.auth
        val currentUser = auth.currentUser
        Log.w("TAH", currentUser?.uid!!)
        getCurrentFlatshare{ flatshare ->
            flatshare.members?.forEach{ id ->
                        getUser(id) { flatmate ->
                            if(flatmate.location != null){
                                val userPosition = LatLng(flatmate.location!!.latitude, flatmate.location!!.longitude)
                                var title = flatmate.first
                                var hue = 359
                                if (id == currentUser.uid) {
                                    title = "Last shared"
                                    hue = 200
                                }
                                map.addMarker(
                                    MarkerOptions()
                                        .icon(BitmapDescriptorFactory.defaultMarker(hue.toFloat()))
                                        .position(userPosition)
                                        .title(title)
                                )
                            }
                        }
            }
        }
    }

    /**
     * Enables the FlatmatesLocation layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Your current location is :\n${p0.latitude} ${p0.longitude}", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}