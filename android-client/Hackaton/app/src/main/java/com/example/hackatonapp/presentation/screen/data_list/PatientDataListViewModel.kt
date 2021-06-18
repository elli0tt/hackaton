package com.example.hackatonapp.presentation.screen.data_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.hackatonapp.data.entities.PatientNoteEntity
import com.example.hackatonapp.data.repository.PatientNoteRepositoryImpl

class PatientDataListViewModel : ViewModel() {

    private val patientNoteRepository = PatientNoteRepositoryImpl()

    val list: LiveData<List<PatientNoteEntity>> = patientNoteRepository.getAllNotes()

    fun onListItemClick(position: Int) {

    }
}