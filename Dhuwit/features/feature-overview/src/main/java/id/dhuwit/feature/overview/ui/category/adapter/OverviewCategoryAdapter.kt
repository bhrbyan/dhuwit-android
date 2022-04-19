package id.dhuwit.feature.overview.ui.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.transaction.model.TransactionCategory
import id.dhuwit.feature.overview.databinding.OverviewCategoryItemBinding

class OverviewCategoryAdapter(private val currencySymbol: String?) :
    RecyclerView.Adapter<OverviewCategoryViewHolder>() {

    private var categories: List<TransactionCategory> = emptyList()
    var listener: OverviewCategoryListener? = null

    fun updateList(categories: List<TransactionCategory>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewCategoryViewHolder {
        return OverviewCategoryViewHolder(
            OverviewCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClickCategory(categories[adapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: OverviewCategoryViewHolder, position: Int) {
        holder.onBind(categories[position], currencySymbol)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}