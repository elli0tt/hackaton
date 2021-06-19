package com.example.hackatonapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.hackatonapp.data.database.AppRoomDatabase
import com.example.hackatonapp.data.database.dao.PatientNoteDao
import com.example.hackatonapp.data.database.entities.PatientNoteEntity
import com.example.hackatonapp.domain.repository.PatientNoteRepository

class PatientNoteRepositoryImpl(context: Context) : PatientNoteRepository {

    private val patientNoteDao: PatientNoteDao = AppRoomDatabase.getDatabase(context).patientNoteDao

    override fun getAllNotes(): LiveData<List<PatientNoteEntity>> {
        return patientNoteDao.getAllNotes()
    }

    override suspend fun insertPatientNote(patientNote: PatientNoteEntity) {
        patientNoteDao.insertPatientNote(patientNote)
    }

    override suspend fun updatePatientNote(patientNote: PatientNoteEntity) {
        patientNoteDao.updatePatientNote(patientNote)
    }

    override suspend fun getNoteById(id: Int): PatientNoteEntity {
        return patientNoteDao.getNoteById(id)
    }
}