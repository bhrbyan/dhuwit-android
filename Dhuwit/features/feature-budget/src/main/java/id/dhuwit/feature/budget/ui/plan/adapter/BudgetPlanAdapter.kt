package id.dhuwit.feature.budget.ui.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.budget.model.BudgetPlanItem
import id.dhuwit.feature.budget.databinding.BudgetPlanItemBinding
import id.dhuwit.storage.Storage

class BudgetPlanAdapter(private val storage: Storage) :
    RecyclerView.Adapter<BudgetPlanViewHolder>() {

    private var budgetPlans: List<BudgetPlanItem> = emptyList()
    var listener: BudgetPlanListener? = null

    fun updateList(budgetPlans: List<BudgetPlanItem>) {
        this.budgetPlans = budgetPlans
        notifyDataSetChanged()
    }

    fun updateItem(categoryId: Long?, budgetPlans: List<BudgetPlanItem>) {
        this.budgetPlans = budgetPlans
        val position = budgetPlans.indexOf(
            budgetPlans.find { it.categoryId == categoryId }
        )

        notifyItemChanged(position)
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
                    val budgetPlan = budgetPlans[adapterPosition]

                    listener?.onClickItem(
                        budgetPlan.categoryId,
                        budgetPlan.amount
                    )
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BudgetPlanViewHolder, position: Int) {
        holder.onBind(budgetPlans[position], storage)
    }

    override fun getItemCount(): Int {
        return budgetPlans.size
    }
}