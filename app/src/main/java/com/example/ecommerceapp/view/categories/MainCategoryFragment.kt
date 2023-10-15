package com.example.ecommerceapp.view.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.BestDealsAdapter
import com.example.ecommerceapp.adapter.BestProductsAdapter
import com.example.ecommerceapp.adapter.SpecialProductsAdapter
import com.example.ecommerceapp.databinding.FragmentAccountBinding
import com.example.ecommerceapp.databinding.FragmentMainCategoryBinding
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.util.Status
import com.example.ecommerceapp.view.AccountFragmentDirections
import com.example.ecommerceapp.viewmodel.AccountViewModel
import com.example.ecommerceapp.viewmodel.category.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs



@AndroidEntryPoint
class MainCategoryFragment : Fragment(){
    private lateinit var viewModel: MainCategoryViewModel
    private lateinit var binding : FragmentMainCategoryBinding
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestProductAdapter: BestProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(MainCategoryViewModel::class.java)
        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nestedScrollMainCategory.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener{ v,_,scrollY,_,_->
                if (v.getChildAt(0).bottom <= v.height + scrollY){
                    viewModel.getProductsFromFirebase()
                }
            }
        )
        setupSpecialProductsRv()
        setupBestDealsRv()
        observeLiveData()
        setupBestProductRv()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData(){
        viewModel.productMessage.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    binding.mainCategoryProgressbar.visibility = View.GONE
                }
                Status.ERROR ->{
                    binding.mainCategoryProgressbar.visibility = View.GONE
                }
                Status.LOADING ->{
                    binding.mainCategoryProgressbar.visibility = View.VISIBLE
                }
            }
        })
        viewModel.specialProducts.observe(viewLifecycleOwner, Observer {
           setDataToLists(it)
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setDataToLists(data: List<FirebaseProduct>?) {
        specialProductsAdapter.productList = data ?: arrayListOf()
        specialProductsAdapter.notifyDataSetChanged()
        bestDealsAdapter.productList = data ?: arrayListOf()
        bestDealsAdapter.notifyDataSetChanged()
        bestProductAdapter.productList = data ?: arrayListOf()
        bestProductAdapter.notifyDataSetChanged()
    }

    private fun setupSpecialProductsRv(){
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductsAdapter
        }
    }

    private fun setupBestDealsRv(){
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = bestDealsAdapter
        }
    }

    private fun setupBestProductRv(){
        bestProductAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = bestProductAdapter
        }
    }
}