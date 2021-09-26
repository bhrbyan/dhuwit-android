package id.dhuwit.feature.category.adapter

import androidx.recyclerview.widget.DiffUtil
import id.dhuwit.core.category.model.Category

class CategoryListDiffUtil : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}