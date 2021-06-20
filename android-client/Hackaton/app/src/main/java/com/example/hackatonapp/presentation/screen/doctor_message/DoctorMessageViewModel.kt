package com.example.hackatonapp.presentation.screen.doctor_message

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.hackatonapp.data.database.entities.DoctorMessageEntity
import com.example.hackatonapp.data.network.NetworkDataSource
import com.example.hackatonapp.data.network.api.DoctorMessagesApi
import com.example.hackatonapp.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorMessageViewModel(application: Application) : AndroidViewModel(application) {

    val messages = MutableLiveData<Resource<List<DoctorMessageEntity>>>()

    val doctorMessagesApi: DoctorMessagesApi = NetworkDataSource.getDoctorMessagesApi()

    init {
        //messages.postValue(generateMessages())
    }

    fun update(token: String) {
        messages.postValue(Resource.Loading())
        doctorMessagesApi.getAllComments(token)
            .enqueue(object : Callback<List<DoctorMessageEntity>> {
                override fun onResponse(
                    call: Call<List<DoctorMessageEntity>>,
                    response: Response<List<DoctorMessageEntity>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            messages.postValue(Resource.Success(it))
                        }
                    } else {
                        messages.postValue(Resource.Error("Something went wrong"))
                    }
                }

                override fun onFailure(call: Call<List<DoctorMessageEntity>>, t: Throwable) {
                    messages.postValue(Resource.Error("Something went wrong"))
                }
            })
    }

    private fun generateMessages(): List<DoctorMessageEntity> {
        val size = 50
        val result = arrayListOf<DoctorMessageEntity>()

        for (i in 0 until size) {
            result.add(
                DoctorMessageEntity(
                    id = 0,
                    patientSNILS = "",
                    doctorId = 0,
                    comment = "dslfjdskfj",
                    dateTime = 0,
                    isSent = false
                )
            )
        }
        return result
    }


}