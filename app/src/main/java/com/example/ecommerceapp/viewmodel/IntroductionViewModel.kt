package com.example.ecommerceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val auth : FirebaseAuth
): ViewModel() {

    private var _authState = MutableLiveData<Resource<Boolean>>()
    val authState : LiveData<Resource<Boolean>>
        get() = _authState

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() = viewModelScope.launch{
        if (auth.currentUser != null){
            if (auth.currentUser!!.isEmailVerified){
                _authState.value = Resource.success(true)
            }else{
                _authState.value = Resource.error( "verify",null)
                signOut()
            }
        }
    }
    private fun signOut() {
        auth.signOut()
    }
}