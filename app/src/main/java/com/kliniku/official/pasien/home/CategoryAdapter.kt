package com.kliniku.official.pasien.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kliniku.official.R
import com.kliniku.official.databinding.ItemCategoryBinding

class CategoryAdapter (
    private val list: List<CategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filter = list[position]
        holder.binding.apply {
            tvFilterLabel.text = filter.label
            imgIcon.setImageResource(filter.icon)
            root.background = ContextCompat.getDrawable(
                root.context,
                if (filter.isSelected) R.drawable.bg_category_selected else R.drawable.bg_category_unselected
            )
        }
    }
}