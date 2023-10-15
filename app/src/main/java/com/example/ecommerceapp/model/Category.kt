package com.example.ecommerceapp.model

sealed class Category(val category : String){
    object Fragrances : Category("Fragrances")
    object Groceries : Category("Groceries")
    object HomeDecoration : Category("Home Decoration")
    object Laptops : Category("Laptops")
    object Skincare : Category("Skincare")
    object SmartPhones : Category(" Smart Phones")
}
