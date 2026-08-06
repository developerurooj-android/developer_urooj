package com.example.doctorappointmentapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val list: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        holder.binding.imgCategory.setImageResource(item.image)
        holder.binding.tvCategoryName.text = item.name
    }

    override fun getItemCount() = list.size
}