package id.dhuwit.feature.account.ui.main.adapter.transaction.header

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.base.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.setting.user.SettingUser
import id.dhuwit.core.transaction.model.TransactionSection
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountMainTransactionHeaderBinding
import id.dhuwit.feature.account.ui.main.adapter.transaction.item.AccountMainTransactionItemAdapter
import id.dhuwit.feature.account.ui.main.adapter.transaction.item.AccountMainTransactionItemListener
import id.dhuwit.uikit.divider.DividerItemDecorationLastItem
import kotlin.math.abs

class AccountMainTransactionHeaderViewHolder(private val binding: AccountMainTransactionHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val context: Context = binding.root.context

    fun onBind(
        transactionSection: TransactionSection,
        settingUser: SettingUser,
        listener: AccountMainTransactionItemListener?
    ) {
        binding.textDate.text = transactionSection.date
        setTotalAmount(transactionSection.totalAmount, settingUser)

        val adapterTransactions =
            AccountMainTransactionItemAdapter(
                transactionSection.transactions,
                settingUser,
                listener
            )
        binding.recyclerViewTransactionItems.apply {
            adapter = adapterTransactions
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            addItemDecoration(
                DividerItemDecorationLastItem(
                    ContextCompat.getDrawable(context, R.drawable.background_divider)
                )
            )
        }
        adapterTransactions.notifyDataSetChanged()
    }

    private fun setTotalAmount(amount: Double, settingUser: SettingUser) {
        val convertedAmount =
            abs(amount).convertPriceWithCurrencyFormat(settingUser.getSymbolCurrency())
        with(binding) {
            textTotalAmountTransaction.text = if (amount >= 0) {
                convertedAmount
            } else {
                context.getString(
                    R.string.account_main_transaction_header_total_amount_negative_format,
                    convertedAmount
                )
            }
        }
    }

}