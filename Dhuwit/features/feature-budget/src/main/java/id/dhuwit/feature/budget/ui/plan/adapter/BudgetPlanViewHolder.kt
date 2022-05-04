package id.dhuwit.feature.budget.ui.plan.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.budget.model.BudgetPlanItem
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.feature.budget.databinding.BudgetPlanItemBinding
import id.dhuwit.storage.Storage

class BudgetPlanViewHolder(private val binding: BudgetPlanItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: BudgetPlanItem, storage: Storage) {
        binding.textCategoryName.text = item.categoryName
        binding.textCategoryAmount.text =
            item.amount?.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())
    }

}