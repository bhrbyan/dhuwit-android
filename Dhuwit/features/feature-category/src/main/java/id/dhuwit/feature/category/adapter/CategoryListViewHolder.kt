package id.dhuwit.feature.category.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.category.model.Category
import id.dhuwit.feature.category.databinding.CategoryListItemBinding

class CategoryListViewHolder(private val binding: CategoryListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(category: Category) {
        binding.textName.text = category.name
    }
}