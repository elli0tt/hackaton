package com.example.hackatonapp.data.network.api

import com.example.hackatonapp.data.database.entities.PatientEntity
import com.example.hackatonapp.data.database.entities.PatientNoteEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PatientApi {

    @GET("{token}/patient")
    fun getPatient(@Path("token") token: String): Call<PatientEntity>

    @GET("{token}/readings")
    fun getAllNotes(@Path("token") token: String): Call<PatientNoteEntity>
}