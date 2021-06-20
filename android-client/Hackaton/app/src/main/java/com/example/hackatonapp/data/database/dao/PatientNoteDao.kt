package com.example.hackatonapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hackatonapp.data.database.entities.PatientNoteEntity

@Dao
interface PatientNoteDao {

    @Query("SELECT * FROM patient_notes_table")
    fun getAllNotes(): LiveData<List<PatientNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatientNote(patientNote: PatientNoteEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePatientNote(patientNote: PatientNoteEntity)

    @Query("SELECT * FROM patient_notes_table WHERE id == :id")
    suspend fun getNoteById(id: Int): PatientNoteEntity

    @Query("DELETE FROM patient_notes_table")
    suspend fun deleteAllNotes()
}