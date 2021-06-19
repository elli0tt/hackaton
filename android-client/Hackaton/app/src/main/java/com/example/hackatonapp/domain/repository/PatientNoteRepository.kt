package com.example.hackatonapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.hackatonapp.data.database.entities.PatientNoteEntity

interface PatientNoteRepository {

    fun getAllNotes(): LiveData<List<PatientNoteEntity>>

    suspend fun insertPatientNote(patientNote: PatientNoteEntity)

    suspend fun updatePatientNote(patientNote: PatientNoteEntity)
}