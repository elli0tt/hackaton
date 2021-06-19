package com.example.hackatonapp.presentation.screen.add_note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

    val patientNoteEntity = MutableLiveData<PatientNoteEntity>()

    private var mode = Mode.ADD
    private var id = 0

    fun start(id: Int) {
        if (id != 0) {
            this.id = id
            mode = Mode.EDIT
            viewModelScope.launch(Dispatchers.IO) {
                patientNoteEntity.postValue(patientNoteRepository.getNoteById(id))
            }
        }
    }

    fun onSaveClick(sys: String, dia: String, pulse: String, activity: String, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val patientNoteEntity = PatientNoteEntity(
                id = id,
                patientSNILS = "",
                dateTimeCreated = Calendar.getInstance().timeInMillis,
                sys = sys.toInt(),
                dia = dia.toInt(),
                pulse = if (pulse.isNotEmpty()) pulse.toInt() else null,
                activity = activity,
                comment = comment
            )
            when (mode) {
                Mode.ADD -> patientNoteRepository.insertPatientNote(patientNoteEntity)
                Mode.EDIT -> patientNoteRepository.updatePatientNote(patientNoteEntity)
            }
        }
    }

    fun findActivityPosition(activity: String, entries: List<String>): Int {
        entries.forEachIndexed { index, entry ->
            if (activity == entry) {
                return index
            }
        }
        return 0
    }
}