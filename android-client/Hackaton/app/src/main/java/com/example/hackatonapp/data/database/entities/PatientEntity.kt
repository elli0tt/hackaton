package com.example.hackatonapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PatientEntity(
    @PrimaryKey
    val id: Int,
    val username: String,
    val fullName: String? = null,
    val snils: String = "",
    @SerializedName("dateOfReg")
    val registrationDate: Long = 0L
) {

}