package com.example.ecommerceapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FirebaseProduct() : Parcelable{
    var id: String? = null
    var brand: String? = null
    var name: String? = null
    var category: String? = null
    var description: String? = null
    var price: Double? = null
    var offerPercentage: Double? = null
    var sizes: String? = null
    var rating: Double? = null
    var stock: Int? = null
    var colors: List<Int>? = null
    var imageUrls: List<String>? = null
    var allSizes: List<String>? = null

    constructor(
        id: String? = null,
        brand: String? = null,
        name: String? = null,
        category: String? = null,
        description: String? = null,
        price: Double? = null,
        offerPercentage: Double? = null,
        sizes: String? = null,
        rating: Double? = null,
        stock: Int? = null,
        colors: List<Int>? = null,
        imageUrls: List<String>? = null,
        allSizes: List<String>? = null
    ) : this() {
        this.id = id
        this.brand = brand
        this.name = name
        this.category = category
        this.description = description
        this.price = price
        this.offerPercentage = offerPercentage
        this.sizes = sizes
        this.rating = rating
        this.stock = stock
        this.colors = colors
        this.imageUrls = imageUrls
        this.allSizes = allSizes
    }
}