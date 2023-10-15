package com.example.ecommerceapp.view.categories

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerceapp.viewmodel.category.SkincareCategoryViewModel
import android.annotation.SuppressLint
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.adapter.BestProductsAdapter
import com.example.ecommerceapp.databinding.FragmentSkincareCategoryBinding
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkincareCategoryFragment : Fragment() {

    private lateinit var viewModel: SkincareCategoryViewModel
    private lateinit var binding: FragmentSkincareCategoryBinding
    private lateinit var bestProductAdapter: BestProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSkincareCategoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SkincareCategoryViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nestedScrollMainCategory.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener{ v, _, scrollY, _, _->
                if (v.getChildAt(0).bottom <= v.height + scrollY){
                    viewModel.getProductsFromFirebase()
                }
            }
        )
        observeLiveData()
        setupBestProductRv()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {
        viewModel.productMessage.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.skincareProgressBar.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.skincareProgressBar.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.skincareProgressBar.visibility = View.VISIBLE
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
        bestProductAdapter = BestProductsAdapter()
        binding.skincareRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }
}
