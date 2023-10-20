package com.example.ecommerceapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.model.PageInfo
import com.example.ecommerceapp.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    val  fireStore : FirebaseFirestore,
    val  category : String,
) : ViewModel(){

    val fireStoreRef = fireStore.collection("products")

    private val _offerProducts = MutableLiveData<List<FirebaseProduct>>()
    val offerProducts : LiveData<List<FirebaseProduct>>
        get() = _offerProducts

    private val _productMessage = MutableLiveData<Resource<List<FirebaseProduct>>>()
    val productMessage : LiveData<Resource<List<FirebaseProduct>>>
        get() = _productMessage

    private val _products = MutableLiveData<List<FirebaseProduct>>()
    val products : LiveData<List<FirebaseProduct>>
        get() = _products


    private val pageInfo = PageInfo()

    init {
        if (category.equals("main")){
            getAllProductsForMainCategory()
            getOfferProductsForMainCategory()
        }else{
            getProductsByCategory()
            getOfferProducts()
        }
    }
    fun getProductsByCategory() = viewModelScope.launch{
        _productMessage.value = Resource.loading(null)
        var productList: MutableList<FirebaseProduct>
        if (!pageInfo.isPagingEnd) {
            fireStoreRef.whereEqualTo("category",category)
                .limit(pageInfo.pageing * 20).get()
                .addOnSuccessListener { result ->
                    productList = result.toObjects(FirebaseProduct::class.java)
                    pageInfo.isPagingEnd = productList == pageInfo.oldBestProducts
                    pageInfo.oldBestProducts = productList
                    _products.value = productList
                    pageInfo.pageing++
                    _productMessage.value = Resource.success(null)
                }.addOnFailureListener { exception ->
                    _productMessage.value = Resource.error(exception.message.toString(), null)
                }
        } else {
            _productMessage.value = Resource.error("error", null)
        }
    }
    fun getAllProductsForMainCategory() = viewModelScope.launch{
        _productMessage.value = Resource.loading(null)
        var productList: MutableList<FirebaseProduct>
        if (!pageInfo.isPagingEnd) {
            fireStoreRef.limit(pageInfo.pageing * 20).get()
                .addOnSuccessListener { result ->
                    productList = result.toObjects(FirebaseProduct::class.java)
                    pageInfo.isPagingEnd = productList == pageInfo.oldBestProducts
                    pageInfo.oldBestProducts = productList
                    _products.value = productList
                    pageInfo.pageing++
                    _productMessage.value = Resource.success(null)
            }.addOnFailureListener { exception ->
                _productMessage.value = Resource.error(exception.message.toString(), null)
            }
        } else {
            _productMessage.value = Resource.error("error", null)
        }
    }

    private fun getOfferProducts() = viewModelScope.launch{
        _productMessage.value = Resource.loading(null)
        fireStoreRef.whereEqualTo("category",category)
            .whereNotEqualTo("offerPercentage",null)
            .get()
            .addOnSuccessListener {
                val products = it.toObjects(FirebaseProduct::class.java)
                _offerProducts.value = products
                _productMessage.value = Resource.success(null)
            }.addOnFailureListener{
                _productMessage.value = it.localizedMessage?.let { error -> Resource.error(error,null) }
            }
    }
    fun getOfferProductsForMainCategory() = viewModelScope.launch{
        _productMessage.value = Resource.loading(null)
        fireStore.collection("products")
            .whereNotEqualTo("offerPercentage",null)
            .get()
            .addOnSuccessListener {
                val products = it.toObjects(FirebaseProduct::class.java)
                _offerProducts.value =products
                _productMessage.value = Resource.success(null)
            }.addOnFailureListener{
                _productMessage.value = it.localizedMessage?.let { error -> Resource.error(error,null) }
            }
    }
}