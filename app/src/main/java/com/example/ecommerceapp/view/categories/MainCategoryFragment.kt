package com.example.ecommerceapp.view.categories

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
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.BestDealsAdapter
import com.example.ecommerceapp.adapter.BestProductsAdapter
import com.example.ecommerceapp.adapter.SpecialProductsAdapter
import com.example.ecommerceapp.databinding.FragmentMainCategoryBinding
import com.example.ecommerceapp.model.FirebaseProduct
import com.example.ecommerceapp.util.Status
import com.example.ecommerceapp.viewmodel.CategoryViewModel
import com.example.ecommerceapp.viewmodel.factory.CategoryViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainCategoryFragment : Fragment(){

    @Inject
    lateinit var fireStore : FirebaseFirestore
    private lateinit var viewModel: CategoryViewModel

    private lateinit var binding : FragmentMainCategoryBinding
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestProductAdapter: BestProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this,
            CategoryViewModelFactory(
                fireStore, "main"
            )
        ).get(CategoryViewModel::class.java)
        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllProductsForMainCategory()
        viewModel.getOfferProductsForMainCategory()
        binding.nestedScrollMainCategory.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener{ v,_,scrollY,_,_->
                if (v.getChildAt(0).bottom <= v.height + scrollY){
                    viewModel.getAllProductsForMainCategory()
                }
            }
        )
        setupSpecialProductsRv()
        setupBestDealsRv()
        observeLiveData()
        setupBestProductRv()

        specialProductsAdapter.onClick = {
            val b = Bundle().apply {
                putParcelable("product",it)
            }
            findNavController().navigate(
                R.id.action_navigation_home_to_productFragment,
                b
            )
        }
        bestDealsAdapter.onClick = {
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
        viewModel.products.observe(viewLifecycleOwner, Observer {
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