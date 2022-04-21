package id.dhuwit.feature.transaction.ui.list.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.transaction.R
import id.dhuwit.feature.transaction.databinding.TransactionListItemBinding

class TransactionListItemViewHolder(private val binding: TransactionListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(transaction: Transaction, currencySymbol: String?) {
        with(binding) {
            setUpAmount(transaction.type, transaction.amount, currencySymbol)
            textCategoryName.text = transaction.category?.name
            textTime.text =
                transaction.createdAt.convertPattern(
                    DateHelper.PATTERN_DATE_DATABASE,
                    DateHelper.PATTERN_TIME
                )
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
                        R.string.transactions_list_item_expense,
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
                        R.string.transactions_list_item_income,
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