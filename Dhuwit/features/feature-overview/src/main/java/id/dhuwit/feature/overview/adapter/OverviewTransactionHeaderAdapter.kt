package id.dhuwit.feature.overview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.overview.databinding.OverviewTransactionHeaderBinding
import id.dhuwit.feature.overview.model.OverviewTransactionItem

class OverviewTransactionHeaderAdapter() :
    RecyclerView.Adapter<OverviewTransactionHeaderViewHolder>() {

    private var overviewTransactionItem: MutableList<OverviewTransactionItem> = ArrayList()
    private var currencySymbol: String? = null

    var listener: OverviewTransactionItemListener? = null

    fun updateList(transactions: List<Transaction>?, currencySymbol: String?) {
        this.currencySymbol = currencySymbol

        overviewTransactionItem.clear()

        transactions?.forEach { transaction ->
            val formattedDate = transaction.date.convertPattern(
                DateHelper.PATTERN_DATE_DATABASE,
                DateHelper.PATTERN_DATE_TRANSACTION
            )

            if (overviewTransactionItem.size <= 0) {
                overviewTransactionItem.add(
                    OverviewTransactionItem().apply {
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
                val lastIndex = overviewTransactionItem.size.minus(1)

                if (formattedDate == overviewTransactionItem[lastIndex].date) {
                    // Add new child to existing section
                    overviewTransactionItem[lastIndex].transactions.add(transaction)
                    overviewTransactionItem[lastIndex].totalAmount = calculateAmountDaily(
                        transaction.type,
                        overviewTransactionItem[lastIndex].totalAmount,
                        transaction.amount
                    )
                } else {
                    // Add new section
                    overviewTransactionItem.add(
                        OverviewTransactionItem().apply {
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
    ): OverviewTransactionHeaderViewHolder {
        return OverviewTransactionHeaderViewHolder(
            OverviewTransactionHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OverviewTransactionHeaderViewHolder, position: Int) {
        holder.onBind(overviewTransactionItem[position], currencySymbol, listener)
    }

    override fun getItemCount(): Int {
        return overviewTransactionItem.size
    }
}