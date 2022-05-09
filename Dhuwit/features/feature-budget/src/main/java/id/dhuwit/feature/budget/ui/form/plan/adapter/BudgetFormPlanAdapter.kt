package id.dhuwit.feature.budget.ui.form.plan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.feature.budget.databinding.BudgetFormPlanItemBinding
import id.dhuwit.storage.Storage

class BudgetFormPlanAdapter(private val storage: Storage) :
    RecyclerView.Adapter<BudgetFormPlanViewHolder>() {

    private var plans: List<BudgetPlan> = emptyList()

    var listener: BudgetFormPlanListener? = null

    fun updateList(plans: List<BudgetPlan>) {
        this.plans = plans
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetFormPlanViewHolder {
        return BudgetFormPlanViewHolder(
            BudgetFormPlanItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClickItemPlan(plans[adapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: BudgetFormPlanViewHolder, position: Int) {
        holder.onBind(plans[position], storage)
    }

    override fun getItemCount(): Int {
        return plans.size

    }
}