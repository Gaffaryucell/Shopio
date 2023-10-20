package com.example.ecommerceapp.viewmodel

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
class ProductDetailsViewModel @Inject constructor(
    private val fireStore : FirebaseFirestore
): ViewModel() {
    val fireStoreRef = fireStore.collection("products")

    private val _productInfo = MutableLiveData<FirebaseProduct>()
    val productInfo: LiveData<FirebaseProduct>
        get() = _productInfo

    private val _productMessage = MutableLiveData<Resource<List<FirebaseProduct>>>()
    val productMessage: LiveData<Resource<List<FirebaseProduct>>>
        get() = _productMessage

    fun getProductDetailsInfo(productID: String) = viewModelScope.launch {
        _productMessage.value = Resource.loading(null)
        val productRef = fireStoreRef.document(productID)

        productRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val productData = documentSnapshot.toObject(FirebaseProduct::class.java)
                    if (productData != null) {
                        // productData, belirli ürünün tüm verilerini içerir
                        // Örneğin, ürün adına erişmek için:
                        val productName = productData.name
                        // veya ürün fiyatına erişmek için:
                        val productPrice = productData.price
                        println("name : " + productName)
                        _productInfo.value = productData
                        // Burada ürün detaylarına ne yapmak istediğinize bağlı olarak işlem yapabilirsiniz.
                    } else {
                        // Döküman belirli bir modelle dönüştürülemedi
                    }
                } else {
                    // Belirtilen belge mevcut değil
                }
            }
            .addOnFailureListener { exception ->
                // Hata durumunda işlemler
            }
    }
}