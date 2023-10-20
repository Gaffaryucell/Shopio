package com.example.ecommerceapp.model

internal data class PageInfo(
    var pageing : Long = 1,
    var oldBestProducts : List<FirebaseProduct> = emptyList(),
    var isPagingEnd : Boolean = false
)