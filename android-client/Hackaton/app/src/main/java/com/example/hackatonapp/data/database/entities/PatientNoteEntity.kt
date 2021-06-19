package com.example.hackatonapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hackatonapp.data.database.entities.PatientNoteEntity.Companion.TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = TABLE_NAME)
data class PatientNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val patientSNILS: String,
    @SerializedName("dateTime")
    val dateTimeCreated: Long = 0L,
    val sys: Int = 0,
    val dia: Int = 0,
    val pulse: Int? = null,
    val activity: String = "",
    val comment: String = ""
) {
    companion object {
        const val TABLE_NAME = "patient_notes_table"
    }
}
