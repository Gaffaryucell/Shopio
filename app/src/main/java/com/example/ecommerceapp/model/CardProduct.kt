package com.example.ecommerceapp.model
 class CardProduct{
    var product : FirebaseProduct? = null
    var quantity : Int? = null
    var size: String? = null
    var color: Int? = null
    constructor()
    constructor(
        product : FirebaseProduct? = null,
        quantity : Int? = null,
        size: String? = null,
        color: Int? = null,
    ){
        this.product = product
        this.quantity = quantity
        this.size = size
        this.color = color
    }
}
