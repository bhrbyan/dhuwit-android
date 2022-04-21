package id.dhuwit.feature.transaction.ui.list.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.transaction.model.TransactionListItem
import id.dhuwit.feature.transaction.R
import id.dhuwit.feature.transaction.databinding.TransactionListHeaderBinding
import id.dhuwit.uikit.divider.DividerLastItemDecoration
import kotlin.math.abs

class TransactionListHeaderViewHolder(private val binding: TransactionListHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        overviewTransactionItem: TransactionListItem,
        currencySymbol: String?,
        listener: TransactionListItemListener?
    ) {
        binding.textDate.text = overviewTransactionItem.date
        setTotalAmount(overviewTransactionItem.totalAmount, currencySymbol)

        val adapterTransactions =
            TransactionListItemAdapter(
                overviewTransactionItem.transactions,
                currencySymbol,
                listener
            )
        binding.recyclerView.apply {
            adapter = adapterTransactions
            layoutManager = LinearLayoutManager(
                binding.root.context,
                LinearLayoutManager.VERTICAL,
                false
            )
            addItemDecoration(
                DividerLastItemDecoration(
                    ContextCompat.getDrawable(context, R.drawable.background_divider)
                )
            )
        }
        adapterTransactions.notifyDataSetChanged()
    }

    private fun setTotalAmount(amount: Double, currencySymbol: String?) {
        val convertedAmount = abs(amount).convertPriceWithCurrencyFormat(currencySymbol)
        with(binding) {
            textAmount.text = if (amount >= 0) {
                convertedAmount
            } else {
                root.context.getString(
                    R.string.transactions_list_header_negative,
                    convertedAmount
                )
            }
        }
    }
}