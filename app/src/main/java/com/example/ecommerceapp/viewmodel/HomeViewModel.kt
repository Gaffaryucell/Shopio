package com.example.ecommerceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.R
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.model.PageInfo
import com.example.ecommerceapp.util.Resource
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fireStore : FirebaseFirestore,
    private val databaseRef : DatabaseReference,
): ViewModel() {

    private val fireStoreRef = fireStore.collection("products")
    private val database = databaseRef.child("products")

    private val _productMessage = MutableLiveData<Resource<List<FirebaseProduct>>>()
    val productMessage : LiveData<Resource<List<FirebaseProduct>>>
        get() = _productMessage

    private val _products = MutableLiveData<List<FirebaseProduct>>()
    val products : LiveData<List<FirebaseProduct>>
        get() = _products



    init {
        getProductsByCategory()
    }
    private fun getProductsByCategory() = viewModelScope.launch{
        _productMessage.value = Resource.loading(null)
        var productList: MutableList<FirebaseProduct>
            fireStoreRef.limit(20)
                .get()
                .addOnSuccessListener { result ->
                    productList = result.toObjects(FirebaseProduct::class.java)
                    _products.value = productList
                    _productMessage.value = Resource.success(null)
                }.addOnFailureListener { exception ->
                    _productMessage.value = Resource.error(exception.message.toString(), null)
                }
    }
}