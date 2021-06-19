package com.example.hackatonapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hackatonapp.data.database.entities.PatientNoteEntity

@Dao
interface PatientNoteDao {

    @Query("SELECT * FROM ${PatientNoteEntity.TABLE_NAME}")
    fun getAllNotes(): LiveData<List<PatientNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatientNote(patientNote: PatientNoteEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePatientNote(patientNote: PatientNoteEntity)

    @Query("SELECT * FROM ${PatientNoteEntity.TABLE_NAME} WHERE id == :id")
    suspend fun getNoteById(id: Int): PatientNoteEntity
}