package com.example.hackatonapp.presentation.screen.sign_in

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

class SignInViewModel(application: Application) : AndroidViewModel(application) {
    val userToken = MutableLiveData<Resource<String>>()

    fun getUserToken(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkDataSource.getPatientApi().signInUser(user.type, user)
            userToken.postValue(handleBreakingNewsResponse(response))
        }
    }

    private fun handleBreakingNewsResponse(response: Response<String>) : Resource<String> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}