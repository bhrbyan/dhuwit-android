package id.dhuwit.feature.budget.ui.plan.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.category.model.Category
import id.dhuwit.feature.budget.databinding.BudgetPlanItemBinding
import id.dhuwit.storage.Storage

class BudgetPlanViewHolder(private val binding: BudgetPlanItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(category: Category, storage: Storage) {
        binding.textCategoryName.text = category.name
    }

}