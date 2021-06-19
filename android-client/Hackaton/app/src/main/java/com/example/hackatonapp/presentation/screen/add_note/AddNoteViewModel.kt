package com.example.hackatonapp.presentation.screen.add_note

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hackatonapp.data.database.entities.PatientNoteEntity
import com.example.hackatonapp.data.network.NetworkDataSource
import com.example.hackatonapp.data.network.body.PostNoteBody
import com.example.hackatonapp.data.repository.PatientNoteRepositoryImpl
import com.example.hackatonapp.domain.repository.PatientNoteRepository
import com.example.hackatonapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AddNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val patientNoteRepository: PatientNoteRepository =
        PatientNoteRepositoryImpl(application.applicationContext)

    val patientNoteEntity = MutableLiveData<PatientNoteEntity>()

    private var mode = Mode.ADD
    private var id = 0

    private val patientApi = NetworkDataSource.getPatientApi()

    val saveResponse = MutableLiveData<Resource<PatientNoteEntity>>()

    fun start(id: Int) {
        if (id != 0) {
            this.id = id
            mode = Mode.EDIT
            viewModelScope.launch(Dispatchers.IO) {
                patientNoteEntity.postValue(patientNoteRepository.getNoteById(id))
            }
        }
    }

    fun onSaveClick(
        sys: String,
        dia: String,
        pulse: String,
        activity: String,
        comment: String,
        token: String
    ) {
        saveResponse.postValue(Resource.Loading())

        val patientNoteEntity = PatientNoteEntity(
            id = id,
            patientSNILS = "",
            dateTime = Calendar.getInstance().timeInMillis,
            sys = sys.toInt(),
            dia = dia.toInt(),
            pulse = if (pulse.isNotEmpty()) pulse.toInt() else null,
            activity = activity,
            comment = comment
        )
        val body = PostNoteBody(
            id = patientNoteEntity.id,
            dateTime = patientNoteEntity.dateTime,
            sys = patientNoteEntity.sys,
            dia = patientNoteEntity.dia,
            pulse = patientNoteEntity.pulse,
            activity = patientNoteEntity.activity,
            comment = patientNoteEntity.comment
        )

        when (mode) {
            Mode.ADD -> {
                patientApi.postNote(
                    token = token,
                    body = body
                ).enqueue(object : Callback<PatientNoteEntity> {
                    override fun onResponse(
                        call: Call<PatientNoteEntity>,
                        response: Response<PatientNoteEntity>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let { body ->
                                viewModelScope.launch(Dispatchers.IO) {
                                    patientNoteRepository.insertPatientNote(body)
                                    saveResponse.postValue(Resource.Success(body))
                                }
                            }
                        } else {
                            saveResponse.postValue(Resource.Error(response.message()))
                        }
                    }

                    override fun onFailure(call: Call<PatientNoteEntity>, t: Throwable) {
                        saveResponse.postValue(Resource.Error(t.message ?: ""))
                    }
                })
            }

            Mode.EDIT -> {
                patientApi.putNote(
                    token = token,
                    body = body
                ).enqueue(object : Callback<PatientNoteEntity> {
                    override fun onResponse(
                        call: Call<PatientNoteEntity>,
                        response: Response<PatientNoteEntity>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let { body ->
                                viewModelScope.launch(Dispatchers.IO) {
                                    patientNoteRepository.updatePatientNote(body)
                                    saveResponse.postValue(Resource.Success(body))
                                }
                            }
                        } else {
                            saveResponse.postValue(Resource.Error(response.message()))
                        }
                    }

                    override fun onFailure(call: Call<PatientNoteEntity>, t: Throwable) {
                        saveResponse.postValue(Resource.Error(t.message ?: ""))
                    }
                })
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