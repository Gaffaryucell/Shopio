package com.example.ecommerceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.databinding.RowBestDealBinding
import com.example.ecommerceapp.databinding.RowSpecialProductBinding
import com.example.ecommerceapp.model.FirebaseProduct

class BestDealsAdapter: RecyclerView.Adapter<BestDealsAdapter.SpecialProductsViewHolder>() {

    inner class SpecialProductsViewHolder(private val binding: RowBestDealBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: FirebaseProduct) {
            binding.apply {
                Glide.with(itemView).load(product.imageUrls?.get(0) ?: "").into(imgBestDeal)

                tvDealProductName.text = product.name
                tvOldPrice.text = product.price.toString()
                tvNewPrice.text = product.price.toString()
                btnSeeProduct.setOnClickListener {

                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<FirebaseProduct>() {
        override fun areItemsTheSame(oldItem: FirebaseProduct, newItem: FirebaseProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FirebaseProduct,
            newItem: FirebaseProduct
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    var productList : List<FirebaseProduct>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        val binding = RowBestDealBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SpecialProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((FirebaseProduct) -> Unit)? = null

}