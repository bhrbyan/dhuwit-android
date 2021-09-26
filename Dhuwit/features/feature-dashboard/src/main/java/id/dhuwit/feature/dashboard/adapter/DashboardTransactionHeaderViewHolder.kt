package id.dhuwit.feature.dashboard.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.feature.dashboard.databinding.DashboardTransactionHeaderBinding

class DashboardTransactionHeaderViewHolder(private val binding: DashboardTransactionHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(date: String, amount: Double, currencySymbol: String?) {
        with(binding) {
            textAmount.text = amount.convertPriceWithCurrencyFormat(currencySymbol)
            textDate.text = date
        }
    }
}