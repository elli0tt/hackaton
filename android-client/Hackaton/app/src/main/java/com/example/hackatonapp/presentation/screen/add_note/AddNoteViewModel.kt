package com.example.hackatonapp.presentation.screen.add_note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackatonapp.data.database.entities.PatientNoteEntity
import com.example.hackatonapp.data.repository.PatientNoteRepositoryImpl
import com.example.hackatonapp.domain.repository.PatientNoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AddNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val patientNoteRepository: PatientNoteRepository =
        PatientNoteRepositoryImpl(application.applicationContext)

    fun onSaveClick(sys: String, dia: String, pulse: String, activity: String, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            patientNoteRepository.insertPatientNote(
                PatientNoteEntity(
                    id = 0,
                    patientSNILS = "",
                    dateTimeCreated = Calendar.getInstance().timeInMillis,
                    sys = sys.toInt(),
                    dia = dia.toInt(),
                    pulse = if (pulse.isNotEmpty()) pulse.toInt() else null,
                    activity = activity,
                    comment = comment
                )
            )
        }
    }
}