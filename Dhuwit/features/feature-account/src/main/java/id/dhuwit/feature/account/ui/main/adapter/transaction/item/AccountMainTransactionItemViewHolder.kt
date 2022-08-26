package id.dhuwit.feature.account.ui.main.adapter.transaction.item

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.base.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.base.extension.gone
import id.dhuwit.core.base.extension.visible
import id.dhuwit.core.base.helper.DateHelper.PATTERN_DATE_DATABASE
import id.dhuwit.core.base.helper.DateHelper.PATTERN_TIME
import id.dhuwit.core.base.helper.DateHelper.convertPattern
import id.dhuwit.core.setting.user.SettingUser
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountMainTransactionItemBinding

class AccountMainTransactionItemViewHolder(private val binding: AccountMainTransactionItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(transaction: Transaction, settingUser: SettingUser) {
        with(binding) {
            setUpAmount(transaction.type, transaction.amount, settingUser.getSymbolCurrency())
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
                            context,
                            R.color.colorTransactionExpense
                        )
                    )

                    text = context.getString(
                        R.string.account_main_transactions_item_expense,
                        convertedAmount
                    )
                }
                is TransactionType.Income -> {
                    setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorTransactionIncome
                        )
                    )

                    text = context.getString(
                        R.string.account_main_transactions_item_income,
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