package com.ethicnology.symbionte


data class User(
    val ID: String,
    val first: String? = "FirstName",
    val last: String? = "LastName",
    val flatshareId: String? = null
)

data class Flatshare(
    val ID: String,
    val admin: String? = null,
    val members: List<String>? = null,
    val name: String? = null
)
