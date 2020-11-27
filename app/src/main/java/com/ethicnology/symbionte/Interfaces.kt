package com.ethicnology.symbionte


data class User(
    val UID: String? = null,
    val first: String? = null,
    val last: String? = null,
    val flatshareId: String? = null
)

data class Flatshare(
    val ID: String? = null,
    val admin: String? = null,
    val members: Array<String>? = null,
    val name: String? = null
)
