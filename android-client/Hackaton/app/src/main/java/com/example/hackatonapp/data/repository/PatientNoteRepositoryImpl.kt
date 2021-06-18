package com.example.hackatonapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.hackatonapp.data.entities.PatientNoteEntity
import com.example.hackatonapp.domain.repository.PatientNoteRepository
import java.util.*
import kotlin.collections.ArrayList

class PatientNoteRepositoryImpl : PatientNoteRepository {

    override fun getAllNotes(): LiveData<List<PatientNoteEntity>> {
        return liveData {
            emit(getMockPatientNotesList())
        }
    }

    private fun getMockPatientNotesList(): List<PatientNoteEntity> {
        val size = 50
        val result = ArrayList<PatientNoteEntity>(size)
        val currentDate = Calendar.getInstance().timeInMillis
        for (i in 0 until size) {
            result.add(
                PatientNoteEntity(
                    id = i,
                    pressure = "120/80 мм р ст.",
                    pulse = "12 уд./мин",
                    dateTimeCreated = currentDate
                )
            )
        }

        return result
    }
}