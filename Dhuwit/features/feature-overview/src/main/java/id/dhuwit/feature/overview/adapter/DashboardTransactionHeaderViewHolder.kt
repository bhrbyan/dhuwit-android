package id.dhuwit.feature.overview.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.feature.overview.R
import id.dhuwit.feature.overview.databinding.DashboardTransactionHeaderBinding
import id.dhuwit.feature.overview.model.DashboardTransaction
import id.dhuwit.uikit.divider.DividerLastItemDecoration
import kotlin.math.abs


class DashboardTransactionHeaderViewHolder(private val binding: DashboardTransactionHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        dashboardTransaction: DashboardTransaction,
        currencySymbol: String?,
        listener: DashboardTransactionItemListener?
    ) {
        binding.textDate.text = dashboardTransaction.date
        setTotalAmount(dashboardTransaction.totalAmount, currencySymbol)

        val adapterTransactions =
            DashboardTransactionItemAdapter(
                dashboardTransaction.transactions,
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