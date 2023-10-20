package com.example.ecommerceapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.ColorsAdapter
import com.example.ecommerceapp.adapter.SizesAdapter
import com.example.ecommerceapp.adapter.ViewPager2Images
import com.example.ecommerceapp.databinding.FragmentProductBinding
import com.example.ecommerceapp.viewmodel.ProductDetailsFragment2ViewModel

class ProductFragment : Fragment() {
    private lateinit var binding : FragmentProductBinding
    private val viewPager2Adapter by lazy { ViewPager2Images() }
    private val colorAdapter = ColorsAdapter()
    private val sizesAdapter = SizesAdapter()
    private val args by navArgs<ProductFragmentArgs>()
    private var selectedColor: Int? = null
    private var selectedSize: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupViewpager()
        setupColorsRv()
        setupSizesRv()
        observeLiveData()


        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        sizesAdapter.onItemClick = {
            selectedSize = it
        }

        colorAdapter.onItemClick = {
            selectedColor = it
        }

        binding.buttonAddToCart.setOnClickListener {
        }



        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description

            if (product.colors.isNullOrEmpty())
                tvProductColors.visibility = View.INVISIBLE
            if (product.allSizes.isNullOrEmpty())
                tvProductSize.visibility = View.INVISIBLE
        }

        viewPager2Adapter.imageList = product.imageUrls as ArrayList<String>
        product.colors?.let {
            colorAdapter.colors = it as ArrayList<Int>
        }
        sizesAdapter.differ.submitList(product.allSizes)
    }

    private fun observeLiveData(){

    }
    private fun setupViewpager() {
        binding.apply {
            viewPagerProductImages.adapter = viewPager2Adapter
        }
    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            adapter = colorAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSizesRv() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

}



