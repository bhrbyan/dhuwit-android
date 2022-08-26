package id.dhuwit.feature.account.ui.main.adapter.transaction.header

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.base.helper.DateHelper.convertPattern
import id.dhuwit.core.setting.user.SettingUser
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionSection
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.account.databinding.AccountMainTransactionHeaderBinding
import id.dhuwit.feature.account.ui.main.adapter.transaction.item.AccountMainTransactionItemListener

class AccountMainTransactionHeaderAdapter(private val settingUser: SettingUser) :
    RecyclerView.Adapter<AccountMainTransactionHeaderViewHolder>() {

    private var transactionSections: MutableList<TransactionSection> = mutableListOf()

    var listener: AccountMainTransactionItemListener? = null

    fun updateList(transactions: List<Transaction>?) {
        transactionSections.clear()

        transactions?.forEach { transaction ->
            val formattedDate = transaction.date.convertPattern(
                id.dhuwit.core.base.helper.DateHelper.PATTERN_DATE_DATABASE,
                id.dhuwit.core.base.helper.DateHelper.PATTERN_DATE_TRANSACTION
            )

            if (transactionSections.size <= 0) {
                // Add new section
                transactionSections.add(
                    TransactionSection().apply {
                        this.date = formattedDate
                        this.totalAmount = calculateAmountDaily(
                            transaction.type,
                            0.0,
                            transaction.amount
                        )
                        this.transactions.add(transaction)
                    }
                )
            } else {
                val lastIndex = transactionSections.size.minus(1)

                if (formattedDate == transactionSections[lastIndex].date) {
                    // Add new child to existing section
                    transactionSections[lastIndex].transactions.add(transaction)
                    transactionSections[lastIndex].totalAmount = calculateAmountDaily(
                        transaction.type,
                        transactionSections[lastIndex].totalAmount,
                        transaction.amount
                    )
                } else {
                    // Add new section
                    transactionSections.add(
                        TransactionSection().apply {
                            this.date = formattedDate
                            this.totalAmount = calculateAmountDaily(
                                transaction.type,
                                0.0,
                                transaction.amount
                            )
                            this.transactions.add(transaction)
                        }
                    )
                }
            }
        }

        notifyDataSetChanged()
    }

    private fun calculateAmountDaily(
        type: TransactionType,
        amountDaily: Double,
        transactionAmount: Double
    ): Double {
        return when (type) {
            is TransactionType.Expense -> {
                amountDaily - transactionAmount
            }
            is TransactionType.Income -> {
                amountDaily + transactionAmount
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountMainTransactionHeaderViewHolder {
        return AccountMainTransactionHeaderViewHolder(
            AccountMainTransactionHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AccountMainTransactionHeaderViewHolder, position: Int) {
        holder.onBind(transactionSections[position], settingUser, listener)
    }

    override fun getItemCount(): Int {
        return transactionSections.size
    }
}