package com.example.hackatonapp.domain.model

data class PatientNote(
    val id: Int,
    val pressure: String = "",
    val pulse: String = "",
    val dateCreated: String = "",
    val timeCreated: String = "",
    val activity: String = ""
)
