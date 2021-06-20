package com.example.hackatonapp.presentation.screen.sign_up

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hackatonapp.data.entities.User
import com.example.hackatonapp.data.network.NetworkDataSource
import com.example.hackatonapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    val userToken = MutableLiveData<Resource<String>>()

    fun getUserToken(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userToken.postValue(Resource.Loading())
        val response = NetworkDataSource.getPatientApi().registrationUser(user, user.type)
        userToken.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<String>): Resource<String> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}