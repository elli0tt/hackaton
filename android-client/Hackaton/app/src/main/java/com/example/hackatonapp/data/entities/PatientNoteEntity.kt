package com.example.hackatonapp.data.entities

data class PatientNoteEntity(
    val id: Int,
    val pressure: String = "",
    val pulse: String = "",
    val dateTimeCreated: Long = 0L
)
