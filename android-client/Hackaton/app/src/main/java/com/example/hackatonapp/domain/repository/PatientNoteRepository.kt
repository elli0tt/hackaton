package com.example.hackatonapp.domain.repository

import androidx.lifecycle.LiveData
import com.example.hackatonapp.data.entities.PatientNoteEntity

interface PatientNoteRepository {

    fun getAllNotes(): LiveData<List<PatientNoteEntity>>
}