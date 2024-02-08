package com.demo.advanced.practical.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.advanced.practical.data.response.Product
import com.demo.advanced.practical.databinding.ItemProductListBinding

class ProductAdapter(
    private val context: Context,
    private val productArrayList: ArrayList<Product> = arrayListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredList: List<Product> = productArrayList.toList()

    class NotificationViewHolder(private val binding: ItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Product) {
            Glide.with(itemView.context).load(data.thumbnail).into(binding.ivProductImage)
            binding.tvProductName.text = data.title
            binding.tvProductDescription.text = data.description
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductListBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = filteredList[position]
        (holder as NotificationViewHolder).bind(data)
    }


    fun filter(query: String?) {
        filteredList = if (query.isNullOrBlank()) {
            productArrayList
        } else {
            productArrayList.filter { it.title!!.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

}


