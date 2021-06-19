package com.example.hackatonapp.data.network

import com.example.hackatonapp.data.network.api.PatientApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkDataSource {

    private const val BASE_URL = "https://95.79.175.202:44320/api"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getPatientApi(): PatientApi {
        return retrofit.create(PatientApi::class.java)
    }
}