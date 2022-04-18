package id.dhuwit.feature.overview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.helper.DateHelper
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.overview.databinding.DashboardTransactionHeaderBinding
import id.dhuwit.feature.overview.model.DashboardTransaction

class DashboardTransactionHeaderAdapter() :
    RecyclerView.Adapter<DashboardTransactionHeaderViewHolder>() {

    private var dashboardTransaction: MutableList<DashboardTransaction> = ArrayList()
    private var currencySymbol: String? = null

    var listener: DashboardTransactionItemListener? = null

    fun updateList(transactions: List<Transaction>?, currencySymbol: String?) {
        this.currencySymbol = currencySymbol

        dashboardTransaction.clear()

        transactions?.forEach { transaction ->
            val formattedDate = transaction.date.convertPattern(
                DateHelper.PATTERN_DATE_DATABASE,
                DateHelper.PATTERN_DATE_TRANSACTION
            )

            if (dashboardTransaction.size <= 0) {
                dashboardTransaction.add(
                    DashboardTransaction().apply {
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
                val lastIndex = dashboardTransaction.size.minus(1)

                if (formattedDate == dashboardTransaction[lastIndex].date) {
                    // Add new child to existing section
                    dashboardTransaction[lastIndex].transactions.add(transaction)
                    dashboardTransaction[lastIndex].totalAmount = calculateAmountDaily(
                        transaction.type,
                        dashboardTransaction[lastIndex].totalAmount,
                        transaction.amount
                    )
                } else {
                    // Add new section
                    dashboardTransaction.add(
                        DashboardTransaction().apply {
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
    ): DashboardTransactionHeaderViewHolder {
        return DashboardTransactionHeaderViewHolder(
            DashboardTransactionHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DashboardTransactionHeaderViewHolder, position: Int) {
        holder.onBind(dashboardTransaction[position], currencySymbol, listener)
    }

    override fun getItemCount(): Int {
        return dashboardTransaction.size
    }
}