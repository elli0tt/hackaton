package com.example.hackatonapp.presentation.screen.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackatonapp.data.repository.PatientNoteRepositoryImpl
import com.example.hackatonapp.domain.repository.PatientNoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val patientNotesRepository: PatientNoteRepository =
        PatientNoteRepositoryImpl(application.applicationContext)

    fun onLogoutClick() {
        viewModelScope.launch(Dispatchers.IO) {
            patientNotesRepository.deleteAllNotes()
        }
    }
}