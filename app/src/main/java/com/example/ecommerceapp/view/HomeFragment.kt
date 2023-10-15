package com.example.ecommerceapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerceapp.adapter.HomeViewPagerAdapter
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.view.categories.FragrancesCategoryFragment
import com.example.ecommerceapp.view.categories.GroceriesCategoryFragment
import com.example.ecommerceapp.view.categories.HomeDecorationCategoryFragment
import com.example.ecommerceapp.view.categories.LaptopsCategoryFragment
import com.example.ecommerceapp.view.categories.MainCategoryFragment
import com.example.ecommerceapp.view.categories.SkincareCategoryFragment
import com.example.ecommerceapp.view.categories.SmartPhonesCategoryFragment
import com.example.ecommerceapp.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel : HomeViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpagerHome.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                // ViewPager2'nin kaydırma durumu değiştiğinde çağrılır
                // ViewPager2'yi yatay kaydırmayı devre dışı bırakın
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    binding.viewpagerHome.isUserInputEnabled = false
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    binding.viewpagerHome.isUserInputEnabled = true
                }
            }
        })

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            SmartPhonesCategoryFragment(),
            LaptopsCategoryFragment(),
            HomeDecorationCategoryFragment(),
            GroceriesCategoryFragment(),
            FragrancesCategoryFragment(),
            SkincareCategoryFragment(),
        )

        val viewpager2Adapter = HomeViewPagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        binding.viewpagerHome.adapter = viewpager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewpagerHome){ tab,position->
             when(position){
                 0-> tab.text = "All"
                 1-> tab.text = "Smartphones"
                 2-> tab.text = "Laptops"
                 3 -> tab.text = "Home Decoration"
                 4 -> tab.text = "Groceries"
                 5-> tab.text = "Fragrances"
                 6-> tab.text = "Skincare"
             }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}