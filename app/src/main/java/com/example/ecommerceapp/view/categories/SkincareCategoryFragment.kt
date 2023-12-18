package com.example.ecommerceapp.view.categories

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.annotation.SuppressLint
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.BestDealsAdapter
import com.example.ecommerceapp.adapter.BestProductsAdapter
import com.example.ecommerceapp.adapter.SpecialProductsAdapter
import com.example.ecommerceapp.databinding.FragmentSkincareCategoryBinding
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.util.Status
import com.example.ecommerceapp.viewmodel.CategoryViewModel
import com.example.ecommerceapp.viewmodel.factory.CategoryViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SkincareCategoryFragment : Fragment() {

    @Inject
    lateinit var fireStore : FirebaseFirestore
    private lateinit var viewModel: CategoryViewModel

    private lateinit var binding: FragmentSkincareCategoryBinding
    private var bestProductAdapter = BestProductsAdapter()
    private var offerProductAdapter= SpecialProductsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSkincareCategoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this,
            CategoryViewModelFactory(
                fireStore, " skincare"
            ))
            .get(CategoryViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nestedScrollMainCategory.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _->
                if (v.getChildAt(0).bottom <= v.height + scrollY){
                    viewModel.getProductsByCategory()
                }
            }
        )
        offerProductAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_productFragment,
                b
            )
        }
        bestProductAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_productFragment,
                b
            )
        }
        observeLiveData()
        setupBestProductRv()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {
        viewModel.productMessage.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.laptopsProgressBar.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.laptopsProgressBar.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.laptopsProgressBar.visibility = View.VISIBLE
                }
            }
        })
        viewModel.products.observe(viewLifecycleOwner, Observer {
            setDataToLists(it)
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setDataToLists(data: List<FirebaseProduct>?) {
        bestProductAdapter.productList = data ?: arrayListOf()
        bestProductAdapter.notifyDataSetChanged()
    }

    private fun setupBestProductRv() {
        binding.rvBestDealsProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),  LinearLayoutManager.HORIZONTAL, false)
            adapter = bestProductAdapter
        }
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }

    }
}
