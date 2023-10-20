package com.example.ecommerceapp.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.databinding.RowColorRvBinding

class ColorsAdapter : RecyclerView.Adapter<ColorsAdapter.ColorViewHolder>() {
    private var selectedPosition = -1

    var colors = ArrayList<Int>()

    inner class ColorViewHolder(val binding: RowColorRvBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        println("onCreateViewHolder")
        val binding = RowColorRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colors.get(position)
        val imageDrawable = ColorDrawable(color)
        println("color : "+color)

        holder.binding.imageColor.setImageDrawable(imageDrawable)
        if (position == selectedPosition){
            holder.binding.apply {
                imageShadow.visibility = ViewGroup.VISIBLE
                imagePicked.visibility = ViewGroup.VISIBLE
            }
        }else{
            holder.binding.apply {
                imageShadow.visibility = ViewGroup.INVISIBLE
                imagePicked.visibility = ViewGroup.INVISIBLE
            }
        }

        holder.itemView.setOnClickListener{
            if (selectedPosition >= 0){
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = position ?: 0
            notifyItemChanged(position)
            onItemClick?.invoke(color)
        }
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    var onItemClick : ((Int) -> Unit)? = null
}
