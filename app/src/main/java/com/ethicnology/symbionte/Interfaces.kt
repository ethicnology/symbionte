package com.ethicnology.symbionte

import com.google.firebase.firestore.GeoPoint


data class User(
    val id: String? = null,
    var first: String? = "FirstName",
    var last: String? = "LastName",
    var flatshareId: String? = null,
    var location: GeoPoint? = null
)

data class Flatshare(
    var name: String? = null,
    val admin: String? = null,
    var members: List<String>? = null,
    var id: String? = null
)
