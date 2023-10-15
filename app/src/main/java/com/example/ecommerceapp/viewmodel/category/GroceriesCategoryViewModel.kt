package com.example.ecommerceapp.viewmodel.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceriesCategoryViewModel  @Inject constructor(
    private val fireStoreDatabase : FirebaseFirestore
): ViewModel() {

    private val _productMessage = MutableLiveData<Resource<List<FirebaseProduct>>>()
    val productMessage : LiveData<Resource<List<FirebaseProduct>>>
        get() = _productMessage

    private val _products = MutableLiveData<List<FirebaseProduct>>()
    val products : LiveData<List<FirebaseProduct>>
        get() = _products


    private val pageInfo = PageInfo()

    init {
        getProductsFromFirebase()
    }




    fun getProductsFromFirebase() = viewModelScope.launch{
        val query = fireStoreDatabase
            .collection("products")
            .whereEqualTo("category","groceries")
            .limit(pageInfo.pageing * 20)
            .get()
        val products = mutableListOf<FirebaseProduct>()
        if (!pageInfo.isPagingEnd) {
            _productMessage.value = Resource.loading(null)
            query.addOnSuccessListener { result ->
                for (doc in result) {
                    val product = doc.toObject(FirebaseProduct::class.java)
                    products.add(product)
                }
                pageInfo.isPagingEnd = products == pageInfo.oldBestProducts
                pageInfo.oldBestProducts = products
                _products.value = products
                pageInfo.pageing++
                _productMessage.value = Resource.success(null)
            }.addOnFailureListener { exception ->
                _productMessage.value = Resource.error(exception.message.toString(), null)
            }
        } else {
            _productMessage.value = Resource.error("error", null)
        }
    }
}
