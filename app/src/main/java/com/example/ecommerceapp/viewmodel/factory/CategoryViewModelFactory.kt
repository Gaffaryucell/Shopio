package com.example.ecommerceapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.viewmodel.CategoryViewModel
import com.google.firebase.firestore.FirebaseFirestore

class CategoryViewModelFactory(
    private val fireStore: FirebaseFirestore,
    private val category: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(fireStore, category) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
