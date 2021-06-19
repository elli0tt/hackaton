package com.example.hackatonapp.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.hackatonapp.data.database.AppRoomDatabase
import com.example.hackatonapp.data.database.dao.PatientNoteDao
import com.example.hackatonapp.data.database.entities.PatientNoteEntity
import com.example.hackatonapp.data.network.NetworkDataSource
import com.example.hackatonapp.domain.repository.PatientNoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientNoteRepositoryImpl(context: Context) : PatientNoteRepository {

    private val patientNoteDao: PatientNoteDao = AppRoomDatabase.getDatabase(context).patientNoteDao

    private val patientApi = NetworkDataSource.getPatientApi()

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

    override fun loadAllNotesFromNetwork() {
        patientApi.getAllNotes("7ea7217c-a414-4f99-a8df-1d4a236cca02")
            .enqueue(object : Callback<List<PatientNoteEntity>> {
                override fun onResponse(
                    call: Call<List<PatientNoteEntity>>,
                    response: Response<List<PatientNoteEntity>>
                ) {
                    GlobalScope.launch(Dispatchers.IO) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                it.forEach {
                                    insertPatientNote(it)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<PatientNoteEntity>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }
}