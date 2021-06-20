package com.example.hackatonapp.data.network.api

import com.example.hackatonapp.data.database.entities.DoctorMessageEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DoctorMessagesApi {

    @GET("{token}/comments")
    fun getAllComments(@Path("token") token: String): Call<List<DoctorMessageEntity>>
}