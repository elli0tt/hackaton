package com.example.hackatonapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_doctor_messages")
data class DoctorMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val patientSNILS: String,
    val doctorId: Int,
    val comment: String,
    val dateTime: Long,
    val isSent: Boolean
)
