package com.example.ecommerceapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.model.UserModel
import com.example.ecommerceapp.util.Resource
import com.example.ecommerceapp.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val database = FirebaseDatabase.getInstance(Util.DATABASE_URL)
    private val reference = database.reference

    private var _authState = MutableLiveData<Resource<Boolean>>()
    val authState : LiveData<Resource<Boolean>>
        get() = _authState

    private var _verification = MutableLiveData<Resource<Boolean>>()
    val verification : LiveData<Resource<Boolean>>
        get() = _verification

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) = viewModelScope.launch{
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    val userId = firebaseAuth.currentUser?.uid ?: ""
                    createUser(email,firstName,lastName,userId)
                }else{
                    _authState.value = Resource.error(task.exception?.localizedMessage ?: "error : try again",null)
                }
            }
    }

    private fun createUser(
        email: String,
        firstName : String,
        lastName : String,
        userId : String,
    )= viewModelScope.launch{
        _authState.value = Resource.loading(null)
        val user = makeUser(userId,"${firstName}_$lastName",email)
        reference
            .child("users")
            .child(userId)
            .setValue(user)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    verify()
                    _authState.value = Resource.success(true)
                }else{
                    _authState.value = Resource.error(it.exception?.localizedMessage ?: "error : try again",null)
                }
            }
    }

    private fun makeUser(userId : String,userName: String,email: String) : UserModel {
        return UserModel(userId,userName,email)
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    private fun verify()= viewModelScope.launch{
        val current = firebaseAuth.currentUser
        current?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                _verification.value = Resource.success(it.isSuccessful)
            } else {
                _verification.value = Resource.error(it.exception?.localizedMessage ?: "error",null)
            }
        }?.addOnFailureListener{
            _verification.value = Resource.error( it.localizedMessage ?: "error",null)
        }
    }
}