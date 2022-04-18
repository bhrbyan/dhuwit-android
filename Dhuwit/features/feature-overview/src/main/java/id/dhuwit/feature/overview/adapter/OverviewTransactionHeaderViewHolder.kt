package id.dhuwit.feature.overview.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.feature.overview.R
import id.dhuwit.feature.overview.databinding.OverviewTransactionHeaderBinding
import id.dhuwit.feature.overview.model.OverviewTransactionItem
import id.dhuwit.uikit.divider.DividerLastItemDecoration
import kotlin.math.abs


class OverviewTransactionHeaderViewHolder(private val binding: OverviewTransactionHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        overviewTransactionItem: OverviewTransactionItem,
        currencySymbol: String?,
        listener: OverviewTransactionItemListener?
    ) {
        binding.textDate.text = overviewTransactionItem.date
        setTotalAmount(overviewTransactionItem.totalAmount, currencySymbol)

        val adapterTransactions =
            OverviewTransactionItemAdapter(
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
                    R.string.dashboard_transactions_header_negative,
                    convertedAmount
                )
            }
        }
    }
}