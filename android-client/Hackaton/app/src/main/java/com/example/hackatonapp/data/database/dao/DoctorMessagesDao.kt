package com.example.hackatonapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hackatonapp.data.database.entities.DoctorMessageEntity

@Dao
interface DoctorMessagesDao {

    @Query("SELECT * FROM table_doctor_messages")
    fun getAllMessage(): LiveData<List<DoctorMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctorMessage(doctorMessage: DoctorMessageEntity)
}