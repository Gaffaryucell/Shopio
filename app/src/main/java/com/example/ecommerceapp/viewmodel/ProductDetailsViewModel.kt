package com.example.ecommerceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.model.CardProduct
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val fireStore : FirebaseFirestore,
    private val firebaseAuth : FirebaseAuth
): ViewModel() {
    private val collection = fireStore.collection("users")
    private val currentUser = firebaseAuth.currentUser

    private val _addCardMessage = MutableLiveData<Resource<String>>()
    val addCardMessage: LiveData<Resource<String>>
        get() = _addCardMessage

    fun addProductIntoCard(product: FirebaseProduct,quantity : Int,size : String?,color: Int?) = viewModelScope.launch {
        val productToUpload = createCardProduct(product,quantity,size,color)
        _addCardMessage.value = Resource.loading(null)
        println("enter")
        collection.document(currentUser?.uid.toString())
            .set(productToUpload)
            .addOnSuccessListener {
                _addCardMessage.value = Resource.success(null)
                println("succes")
            }
            .addOnFailureListener { e ->
                _addCardMessage.value = Resource.error("error : "+e.localizedMessage,null)
                // Belge eklenirken bir hata oluşursa buraya ulaşılır
            }

    }

    private fun createCardProduct(
        product: FirebaseProduct,
        quantity: Int,
        size: String?,
        color: Int?,
    ): CardProduct {
        return CardProduct(product, quantity, size, color)
    }
}

