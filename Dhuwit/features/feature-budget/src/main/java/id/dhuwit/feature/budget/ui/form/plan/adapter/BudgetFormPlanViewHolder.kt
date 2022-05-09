package id.dhuwit.feature.budget.ui.form.plan.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.budget.model.BudgetPlan
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.feature.budget.databinding.BudgetFormPlanItemBinding
import id.dhuwit.storage.Storage

class BudgetFormPlanViewHolder(private val binding: BudgetFormPlanItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(budgetPlan: BudgetPlan, storage: Storage) {
        binding.textCategoryName.text = budgetPlan.category.name
        binding.textPlanAmount.text =
            budgetPlan.budgetAmount.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
    }

}