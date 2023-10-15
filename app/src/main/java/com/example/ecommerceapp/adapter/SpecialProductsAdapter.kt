package com.example.ecommerceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.databinding.RowSpecialProductBinding
import com.example.ecommerceapp.model.FirebaseProduct

class SpecialProductsAdapter : RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {

    inner class SpecialProductsViewHolder(private val binding: RowSpecialProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: FirebaseProduct) {
            binding.apply {
                Glide.with(itemView).load(product.imageUrls?.get(0) ?: "").into(imageSpecialRvItem)
                tvSpecialProductName.text = product.name
                tvSpecialPrdouctPrice.text = product.price.toString()
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
        val binding = RowSpecialProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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