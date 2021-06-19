package com.example.hackatonapp.data.entities

data class User(
    val username: String,
    val password: String,
    val type: String =  "pat",
    val snils: String? = null
)
