package com.example.ecommerceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ecommerceapp.databinding.TabLayoutItemBinding

class HomeViewPagerAdapter(
    private val fragments : List<Fragment>,
    fm : FragmentManager,
    lifecycle : Lifecycle
) : FragmentStateAdapter(fm,lifecycle){

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}

/*
class HomeViewPagerAdapter2(
    private val categories : List<String>,
) : RecyclerView.Adapter<HomeViewPagerAdapter2.HomeViewHolder2>(){
    inner class HomeViewHolder2(val binding : TabLayoutItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewPagerAdapter2.HomeViewHolder2 {
        val binding = TabLayoutItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeViewHolder2(binding)
    }

    override fun onBindViewHolder(holder: HomeViewPagerAdapter2.HomeViewHolder2, position: Int) {
        val text = categories[position]
        holder.binding.oneLineText.text = text
    }


    override fun getItemCount(): Int {
        return categories.size
    }

}
 */