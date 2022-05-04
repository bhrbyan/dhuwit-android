package id.dhuwit.feature.budget.ui.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.category.model.Category
import id.dhuwit.feature.budget.databinding.BudgetPlanItemBinding
import id.dhuwit.storage.Storage

class BudgetPlanAdapter(private val storage: Storage) :
    RecyclerView.Adapter<BudgetPlanViewHolder>() {

    private var categories: List<Category> = emptyList()
    var listener: BudgetPlanListener? = null

    fun updateList(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetPlanViewHolder {
        return BudgetPlanViewHolder(
            BudgetPlanItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClickItem()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BudgetPlanViewHolder, position: Int) {
        holder.onBind(categories[position], storage)
    }

    override fun getItemCount(): Int {
        return categories?.size
    }
}