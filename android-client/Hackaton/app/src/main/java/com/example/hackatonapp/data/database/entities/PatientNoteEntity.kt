package com.example.hackatonapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_notes_table")
data class PatientNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val patientSNILS: String,
    val dateTime: Long = 0L,
    val sys: Int = 0,
    val dia: Int = 0,
    val pulse: Int? = null,
    val activity: String = "",
    val comment: String = ""
)
