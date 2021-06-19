package com.example.hackatonapp.data.network

import com.example.hackatonapp.data.network.api.PatientApi
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkDataSource {

    private const val BASE_URL = "http://95.79.175.202:14332/api/"

    private val logging = HttpLoggingInterceptor()

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    fun getPatientApi(): PatientApi {
        return retrofit.create(PatientApi::class.java)
    }
}