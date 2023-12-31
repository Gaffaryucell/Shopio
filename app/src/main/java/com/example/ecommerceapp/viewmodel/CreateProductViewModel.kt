package com.example.ecommerceapp.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.model.Product
import com.example.ecommerceapp.model.ProductX
import com.example.ecommerceapp.util.Resource
import com.example.ecommerceapp.util.Util.DATABASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val fireStore : FirebaseFirestore,
    private val auth : FirebaseAuth,
    private val storage : FirebaseStorage
) : ViewModel() {
    private val storageReference = storage.reference
    private val collection = fireStore.collection("users")

    private val _uploadProductStatus = MutableLiveData<Resource<String>>()
    val uploadProductStatus: LiveData<Resource<String>> = _uploadProductStatus

    fun createProduct(
        id: String,
        name: String,
        category: String,
        description: String,
        price: Double,
        offerPercentage: Double,
        sizes: String,
        colors: List<Int>,
        imageList: List<ByteArray>
    ) {
        val product = FirebaseProduct()
        product.id = id
        product.name = name
        product.category = category
        product.description = description
        product.price = price
        product.offerPercentage = offerPercentage
        product.sizes = sizes
        product.colors = colors
        uploadPictures(imageList, product)
    }


    private fun uploadPictures(bAList: List<ByteArray>, product: FirebaseProduct) =
        viewModelScope.launch {
            _uploadProductStatus.value = Resource.loading(null)
            val newList = ArrayList<String>()
            for (i in bAList) {
                val photoFileName = "${UUID.randomUUID()}.jpg"
                val photoRef = storageReference.child("products/${product.id}/$photoFileName")
                photoRef.putBytes(i).addOnSuccessListener {
                    photoRef.downloadUrl
                        .addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            newList.add(imageUrl)
                            if (i.contentEquals(bAList.last())) {
                                uploadToFirebase(product)
                            }
                        }
                        .addOnFailureListener { exception ->
                            // URL alınamazsa burada hata işleme kodlarınızı yazabilirsiniz.
                            _uploadProductStatus.value =
                                Resource.error("cannot acces url", exception.localizedMessage)
                        }
                }.addOnFailureListener { exception ->
                    // Yükleme başarısız olursa, burada hata işleme kodlarınızı yazabilirsiniz.
                    _uploadProductStatus.value =
                        Resource.error("cannot upload photo", exception.localizedMessage)
                }
            }
        }
    private fun uploadToFirebase(product: FirebaseProduct) = viewModelScope.launch{
        _uploadProductStatus.value = Resource.loading("loaded in Database")

        collection.document(auth.uid.toString())
            .set(product)
            .addOnSuccessListener {
                _uploadProductStatus.value = Resource.success("loaded in Database")
            }
            .addOnFailureListener{ e ->
                _uploadProductStatus.value = Resource.success("loaded in Database")
            }
        }
}
