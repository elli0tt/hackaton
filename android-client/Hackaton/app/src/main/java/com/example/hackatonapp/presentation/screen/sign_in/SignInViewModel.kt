package com.example.hackatonapp.presentation.screen.sign_in

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hackatonapp.data.entities.User
import com.example.hackatonapp.data.network.NetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(application: Application) : AndroidViewModel(application) {
    val userToken = MutableLiveData<String>()

    fun getUserToken(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            userToken.postValue(NetworkDataSource.getPatientApi().signInUser(user.type, user))
        }
    }
}