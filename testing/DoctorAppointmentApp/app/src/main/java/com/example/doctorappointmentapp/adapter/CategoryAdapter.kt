package com.example.doctorappointmentapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentapp.R
import com.example.doctorappointmentapp.model.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onClick: ((Category) -> Unit)? = null
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.tvCatName.text = category.name
        holder.ivCatIcon.setImageResource(category.icon)
        holder.itemView.setOnClickListener { onClick?.invoke(category) }
    }

    override fun getItemCount(): Int = categories.size

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCatIcon: ImageView = view.findViewById(R.id.ivCatIcon)
        val tvCatName: TextView  = view.findViewById(R.id.tvCatName)
    }
}
