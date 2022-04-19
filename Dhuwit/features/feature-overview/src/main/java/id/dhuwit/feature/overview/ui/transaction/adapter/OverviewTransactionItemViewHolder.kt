package id.dhuwit.feature.overview.ui.transaction.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_DATABASE
import id.dhuwit.core.helper.DateHelper.PATTERN_TIME
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.overview.R
import id.dhuwit.feature.overview.databinding.OverviewTransactionItemBinding

class OverviewTransactionItemViewHolder(private val binding: OverviewTransactionItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(transaction: Transaction, currencySymbol: String?) {
        with(binding) {
            setUpAmount(transaction.type, transaction.amount, currencySymbol)
            textCategoryName.text = transaction.category?.name
            textTime.text =
                transaction.createdAt.convertPattern(PATTERN_DATE_DATABASE, PATTERN_TIME)
            setUpNote(transaction.note)
        }
    }

    private fun setUpAmount(type: TransactionType, amount: Double, currencySymbol: String?) {
        val convertedAmount: String = amount.convertPriceWithCurrencyFormat(currencySymbol)
        binding.textAmount.apply {
            when (type) {
                is TransactionType.Expense -> {
                    setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.colorTransactionExpense
                        )
                    )

                    text = binding.root.context.getString(
                        R.string.overview_transactions_item_expense,
                        convertedAmount
                    )
                }
                is TransactionType.Income -> {
                    setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.colorTransactionIncome
                        )
                    )

                    text = binding.root.context.getString(
                        R.string.overview_transactions_item_income,
                        convertedAmount
                    )
                }
            }
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