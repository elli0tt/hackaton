package com.example.hackatonapp.domain.repository

import com.example.hackatonapp.data.database.entities.DoctorMessageEntity
import retrofit2.Call

interface DoctorMessageRepository {

    fun getAllMessages(): Call<List<DoctorMessageEntity>>
}