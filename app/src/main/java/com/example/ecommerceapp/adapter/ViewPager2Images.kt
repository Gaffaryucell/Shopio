package com.example.ecommerceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.databinding.RowBestDealBinding
import com.example.ecommerceapp.databinding.ViewpagerImageItemBinding

class ViewPager2Images : RecyclerView.Adapter<ViewPager2Images.ViewPagerViewHolder>() {

    var imageList = ArrayList<String>()

    inner class ViewPagerViewHolder(val binding : ViewpagerImageItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding = ViewpagerImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewPagerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val product = imageList[position]
        Glide.with(holder.itemView.context).load(product)
            .into(holder.binding.imageProductDetails)
    }
}