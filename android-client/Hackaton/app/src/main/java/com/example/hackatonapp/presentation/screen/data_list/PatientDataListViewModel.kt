package com.example.hackatonapp.presentation.screen.data_list

import android.app.Application
import android.widget.Filter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.hackatonapp.R
import com.example.hackatonapp.data.database.entities.PatientNoteEntity
import com.example.hackatonapp.data.repository.PatientNoteRepositoryImpl
import com.example.hackatonapp.domain.model.PatientNote
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PatientDataListViewModel(application: Application) : AndroidViewModel(application) {

    private val patientNoteRepository = PatientNoteRepositoryImpl(application.applicationContext)

    private var searchQuery: String = ""

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val allNotes: LiveData<List<PatientNote>> =
        Transformations.map(patientNoteRepository.getAllNotes()) {
            it.map { patientNoteEntity ->
                val date = Date(patientNoteEntity.dateTimeCreated)
                PatientNote(
                    id = patientNoteEntity.id,
                    pressure = application.getString(
                        R.string.patient_data_list_pressure,
                        patientNoteEntity.sys,
                        patientNoteEntity.dia
                    ),
                    pulse = application.getString(
                        R.string.patient_data_list_pulse,
                        patientNoteEntity.pulse
                    ),
                    dateCreated = dateFormat.format(date),
                    timeCreated = timeFormat.format(date),
                    activity = patientNoteEntity.activity
                )
            }
        }

    val listToShow = MutableLiveData<List<PatientNote>>()

    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            getMockPatientNotesList().forEach {
//                patientNoteRepository.insertPatientNote(it)
//            }
//        }
    }

    private fun getMockPatientNotesList(): List<PatientNoteEntity> {
        val size = 50
        val result = ArrayList<PatientNoteEntity>(size)
        val currentDate = Calendar.getInstance().timeInMillis
        for (i in 0 until size) {
            result.add(
                PatientNoteEntity(
                    id = i,
                    sys = 160,
                    dia = 50,
                    pulse = 66,
                    dateTimeCreated = currentDate,
                    patientSNILS = ""
                )
            )
        }
        result.add(
            PatientNoteEntity(
                id = 100,
                sys = 180,
                dia = 200,
                pulse = 66,
                dateTimeCreated = currentDate,
                patientSNILS = ""
            )
        )
        result.add(
            PatientNoteEntity(
                id = 101,
                sys = 120,
                dia = 400,
                pulse = 666,
                dateTimeCreated = currentDate,
                patientSNILS = ""
            )
        )

        return result
    }

    fun onAllNotesUpdate() {
        filter.filter(searchQuery)
    }

    fun onListItemClick(position: Int): Int {
        return listToShow.value?.get(position)?.id ?: 0
    }

    private val filter = object : Filter() {
        override fun performFiltering(query: CharSequence): FilterResults {
            var result: MutableList<PatientNote> = arrayListOf()
            if (query.isEmpty()) {
                result = allNotes.value?.toMutableList() ?: arrayListOf()
            } else {
                allNotes.value?.forEach { patientNote ->
                    if (patientNote.pressure.contains(query) ||
                        patientNote.pulse.contains(query) ||
                        patientNote.dateCreated.contains(query) ||
                        patientNote.timeCreated.contains(query)
                    ) {
                        result.add(patientNote)
                    }
                }
            }

            val filterResults = FilterResults()
            filterResults.values = result
            return filterResults
        }

        override fun publishResults(
            query: CharSequence,
            filterResults: FilterResults
        ) {
            listToShow.value = filterResults.values as List<PatientNote>
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        filter.filter(query)
    }
}