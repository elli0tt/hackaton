package com.example.hackatonapp.data.network.api

import com.example.hackatonapp.data.database.entities.PatientEntity
import com.example.hackatonapp.data.database.entities.PatientNoteEntity
import com.example.hackatonapp.data.entities.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface PatientApi {

    @GET("{token}/patient")
    fun getPatient(@Path("token") token: String): Call<PatientEntity>

    @GET("{token}/readings")
    fun getAllNotes(@Path("token") token: String): Call<PatientNoteEntity>

    @Headers("Content-Type: application/json")
    @POST("reg/{type}")
    suspend fun registrationUser(@Body user: User, @Path("type") type: String): String

    @Headers("Content-Type: application/json")
    @POST("log/{type}")
    suspend fun signInUser(@Path("type") type: String, @Body user: User): Response<String>
}