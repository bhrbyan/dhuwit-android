package id.dhuwit.feature.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.category.model.Category
import id.dhuwit.feature.category.databinding.CategoryListItemBinding

class CategoryListAdapter : ListAdapter<Category, CategoryListViewHolder>(CategoryListDiffUtil()) {
    var listener: CategoryListListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        return CategoryListViewHolder(
            CategoryListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onSelectCategory(getItem(adapterPosition))
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}