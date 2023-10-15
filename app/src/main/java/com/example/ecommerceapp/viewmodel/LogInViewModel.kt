package com.example.ecommerceapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.model.UserModel
import com.example.ecommerceapp.util.Resource
import com.example.ecommerceapp.util.Util.DATABASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LogInViewModel @Inject constructor(
    private val auth: FirebaseAuth
): ViewModel() {

    private var _authState = MutableLiveData<Resource<Boolean>>()
    val authState : LiveData<Resource<Boolean>>
        get() = _authState

    init {
        getCurrentUser()
    }


    fun signIn(
        email: String,
        password: String
    ) = viewModelScope.launch{
        _authState.value = Resource.loading(null)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    if (task.result.user!!.isEmailVerified){
                        _authState.value = Resource.success(true)
                    }else{
                        _authState.value = Resource.error( "verify",null)
                        signOut()
                    }
                }else{
                    _authState.value = Resource.error(task.exception?.localizedMessage ?: "error : try again",null)
                }
        }
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
    fun sendResetPasswordEmail(email: String,context: Context) = viewModelScope.launch{
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Email sent successfully
                    Toast.makeText(context,
                        "Reset password email sent to $email",
                        Toast.LENGTH_SHORT)
                        .show()

                } else {
                    // Email sending failed
                    Toast.makeText(context,
                        "Failed to send reset password email: ${task.exception?.message}",
                        Toast.LENGTH_SHORT)
                        .show()

                }
            }
    }

    private fun resetPassWord(password: String){
        val user = FirebaseAuth.getInstance().currentUser

        user!!.updatePassword(password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Update Success")
            } else {
                println("Erorr Update")
            }
        }
    }
}