package id.dhuwit.feature.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.category.model.Category
import id.dhuwit.feature.category.databinding.CategoryListItemBinding

class CategoryListAdapter : RecyclerView.Adapter<CategoryListViewHolder>() {

    var listener: CategoryListListener? = null
    var categories: ArrayList<Category> = ArrayList()

    fun updateList(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return categories.size
    }

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
                    listener?.onSelectCategory(categories[adapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        holder.bind(categories[position])
    }
}