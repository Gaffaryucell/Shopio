package com.example.ecommerceapp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.databinding.RowProductItemBinding
import com.example.ecommerceapp.model.FirebaseProduct

class BestProductsAdapter : RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>() {

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


    inner class BestProductsViewHolder(private val binding: RowProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: FirebaseProduct) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(product.imageUrls?.get(0))
                    .into(imgProduct)

                tvName.text = product.name
                tvNewPrice.text = product.price.toString()
                tvPrice.text = product.price.toString()
                tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                imgFavorite.setOnClickListener {
                    // TODO: Add your favorite button click logic here
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        val binding = RowProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BestProductsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick: ((FirebaseProduct) -> Unit)? = null

}