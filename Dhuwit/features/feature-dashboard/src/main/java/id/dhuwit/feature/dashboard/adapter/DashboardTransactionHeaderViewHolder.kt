package id.dhuwit.feature.dashboard.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.feature.dashboard.R
import id.dhuwit.feature.dashboard.databinding.DashboardTransactionHeaderBinding
import kotlin.math.abs

class DashboardTransactionHeaderViewHolder(private val binding: DashboardTransactionHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(date: String, amount: Double, currencySymbol: String?) {
        setUpAmount(amount, currencySymbol)
        binding.textDate.text = date
    }

    private fun setUpAmount(amount: Double, currencySymbol: String?) {
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