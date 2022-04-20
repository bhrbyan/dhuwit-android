package id.dhuwit.feature.overview.ui.account.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.transaction.model.TransactionAccount
import id.dhuwit.feature.overview.R
import id.dhuwit.feature.overview.databinding.OverviewAccountItemBinding

class OverviewAccountViewHolder(
    private val binding: OverviewAccountItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: TransactionAccount, currencySymbol: String?) {
        binding.textAccountName.text = item.accountName
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