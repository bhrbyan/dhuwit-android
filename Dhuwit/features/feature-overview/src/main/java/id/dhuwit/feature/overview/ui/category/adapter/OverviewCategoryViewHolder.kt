package id.dhuwit.feature.overview.ui.category.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.transaction.model.TransactionCategory
import id.dhuwit.feature.overview.R
import id.dhuwit.feature.overview.databinding.OverviewCategoryItemBinding

class OverviewCategoryViewHolder(
    private val binding: OverviewCategoryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: TransactionCategory, currencySymbol: String?) {
        binding.textCategoryName.text = item.categoryName
        binding.textTotalTransaction.text = if (item.totalTransaction <= 1) {
            binding.root.context.getString(
                R.string.overview_category_total_transaction_less_than_one,
                item.totalTransaction
            )
        } else {
            binding.root.context.getString(
                R.string.overview_category_total_transaction_more_than_one,
                item.totalTransaction
            )
        }
        binding.textTotalAmountTransaction.text =
            item.totalAmountTransaction.convertPriceWithCurrencyFormat(currencySymbol)

    }

}