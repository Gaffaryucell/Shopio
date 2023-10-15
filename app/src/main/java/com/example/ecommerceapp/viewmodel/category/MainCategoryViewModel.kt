package com.example.ecommerceapp.viewmodel.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.model.Product
import com.example.ecommerceapp.util.Resource
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val fireStoreDatabase : FirebaseFirestore
): ViewModel() {

    private val _productMessage = MutableLiveData<Resource<List<FirebaseProduct>>>()
    val productMessage : LiveData<Resource<List<FirebaseProduct>>>
        get() = _productMessage

    private val _specialProducts = MutableLiveData<List<FirebaseProduct>>()
    val specialProducts : LiveData<List<FirebaseProduct>>
        get() = _specialProducts

    private val _bestProducts = MutableLiveData<Resource<FirebaseProduct>>()
    val bestProducts : LiveData<Resource<FirebaseProduct>>
        get() = _bestProducts

    private val _bestDeals = MutableLiveData<Resource<List<FirebaseProduct>>>()
    val bestDeals : LiveData<Resource<List<FirebaseProduct>>>
        get() = _bestDeals

    private val pageInfo = PageInfo()

    init {
        getProductsFromFirebase()
        fetchBestDeals()
        fetchBestProducts()
    }


    private fun fetchBestDeals() =  viewModelScope.launch {

    }

    private fun fetchBestProducts() =  viewModelScope.launch {

    }
    //.whereEqualTo(
    //            "category","groceries"
    //        )
    fun getProductsFromFirebase()  = viewModelScope.launch{
        val query = fireStoreDatabase.collection("products").limit(pageInfo.pageing * 20).get()
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
                _specialProducts.value = products
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
internal data class PageInfo(
    var pageing : Long = 1,
    var oldBestProducts : List<FirebaseProduct> = emptyList(),
    var isPagingEnd : Boolean = false
)