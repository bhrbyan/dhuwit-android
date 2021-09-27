package id.dhuwit.feature.dashboard.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_DATABASE
import id.dhuwit.core.helper.DateHelper.PATTERN_TIME
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.feature.dashboard.databinding.DashboardTransactionItemBinding

class DashboardTransactionItemViewHolder(private val binding: DashboardTransactionItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(transaction: Transaction, currencySymbol: String?) {
        with(binding) {
            textAmount.text = transaction.amount.convertPriceWithCurrencyFormat(currencySymbol)
            textCategoryName.text = transaction.category?.name
            textTime.text =
                transaction.createdAt.convertPattern(PATTERN_DATE_DATABASE, PATTERN_TIME)
            setUpNote(transaction.note)
        }
    }

    private fun setUpNote(note: String?) {
        with(binding) {
            if (note.isNullOrEmpty()) {
                textNote.gone()
            } else {
                textNote.visible()
                textNote.text = note
            }
        }
    }
}