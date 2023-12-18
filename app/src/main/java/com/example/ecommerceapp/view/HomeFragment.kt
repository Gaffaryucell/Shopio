package com.example.ecommerceapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.BestProductsAdapter
import com.example.ecommerceapp.adapter.HomeViewPagerAdapter
import com.example.ecommerceapp.adapter.SpecialProductsAdapter
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.databinding.FragmentSmartphonesCategoryBinding
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.util.Status
import com.example.ecommerceapp.view.categories.FragrancesCategoryFragment
import com.example.ecommerceapp.view.categories.GroceriesCategoryFragment
import com.example.ecommerceapp.view.categories.HomeDecorationCategoryFragment
import com.example.ecommerceapp.view.categories.LaptopsCategoryFragment
import com.example.ecommerceapp.view.categories.MainCategoryFragment
import com.example.ecommerceapp.view.categories.SkincareCategoryFragment
import com.example.ecommerceapp.view.categories.SmartPhonesCategoryFragment
import com.example.ecommerceapp.viewmodel.CategoryViewModel
import com.example.ecommerceapp.viewmodel.HomeViewModel
import com.example.ecommerceapp.viewmodel.factory.CategoryViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private val bestProductAdapter by lazy { BestProductsAdapter() }
    private val offerProductAdapter by lazy { SpecialProductsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,).get(HomeViewModel::class.java)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        setupRv()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {
        viewModel.productMessage.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                }
                Status.ERROR -> {
                }
                Status.LOADING -> {
                }
            }
        })
        viewModel.products.observe(viewLifecycleOwner, Observer {
            setDataToLists(it)
            setOfferProductsRv(it)
        })
    }

    private fun setOfferProductsRv(it: List<FirebaseProduct>?) {
        offerProductAdapter.productList = it ?: arrayListOf()
        offerProductAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setDataToLists(data: List<FirebaseProduct>?) {
        bestProductAdapter.productList = data ?: arrayListOf()
        bestProductAdapter.notifyDataSetChanged()
    }

    private fun setupRv() {
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}