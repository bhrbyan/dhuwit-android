package id.dhuwit.feature.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_DATABASE
import id.dhuwit.core.helper.DateHelper.PATTERN_DATE_TRANSACTION
import id.dhuwit.core.helper.DateHelper.convertPattern
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.core.transaction.model.TransactionType
import id.dhuwit.feature.dashboard.databinding.DashboardTransactionHeaderBinding
import id.dhuwit.feature.dashboard.databinding.DashboardTransactionItemBinding
import id.dhuwit.feature.dashboard.model.DashboardTransactionSection

class DashboardTransactionAdapter(
    context: Context,
    transactionSections: List<DashboardTransactionSection>
) :
    SectionRecyclerViewAdapter<
            DashboardTransactionSection,
            Transaction,
            DashboardTransactionHeaderViewHolder,
            DashboardTransactionItemViewHolder>(
        context,
        transactionSections
    ) {

    private var sections: MutableList<DashboardTransactionSection> = ArrayList()
    private var currencySymbol: String? = null
    var listener: DashboardTransactionItemListener? = null

    fun updateList(transactions: List<Transaction>?, currencySymbol: String?) {
        clearSection()

        this.currencySymbol = currencySymbol
        transactions?.forEach { transaction ->
            val formattedDate = transaction.date.convertPattern(
                PATTERN_DATE_DATABASE,
                PATTERN_DATE_TRANSACTION
            )

            if (sections.size <= 0) {
                // Create first section
                val section = DashboardTransactionSection()
                    .apply {
                        this.date = formattedDate
                        this.transactions.add(transaction)
                        this.amountDaily = calculateAmountDaily(
                            transaction.type,
                            amountDaily,
                            transaction.amount
                        )
                    }

                sections.add(section)
            } else {
                val lastIndex = sections.size.minus(1)

                if (formattedDate == sections[lastIndex].dateHeader) {
                    // Add new child to existing section
                    sections[lastIndex].transactions.add(transaction)
                    sections[lastIndex].amountDaily = calculateAmountDaily(
                        transaction.type,
                        sections[lastIndex].amountDaily,
                        transaction.amount
                    )
                } else {
                    // Add new section
                    val section = DashboardTransactionSection()
                        .apply {
                            this.date = formattedDate
                            this.transactions.add(transaction)
                            this.amountDaily = calculateAmountDaily(
                                transaction.type,
                                amountDaily,
                                transaction.amount
                            )
                        }

                    sections.add(section)
                }
            }
        }

        notifyDataChanged(sections)
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

    override fun onCreateSectionViewHolder(
        parent: ViewGroup?,
        position: Int
    ): DashboardTransactionHeaderViewHolder {
        return DashboardTransactionHeaderViewHolder(
            DashboardTransactionHeaderBinding.inflate(
                LayoutInflater.from(parent?.context),
                parent,
                false
            )
        )
    }

    override fun onCreateChildViewHolder(
        parent: ViewGroup?,
        position: Int
    ): DashboardTransactionItemViewHolder {
        return DashboardTransactionItemViewHolder(
            DashboardTransactionItemBinding.inflate(
                LayoutInflater.from(parent?.context),
                parent,
                false
            )
        )
    }

    override fun onBindSectionViewHolder(
        holder: DashboardTransactionHeaderViewHolder?,
        sectionPosition: Int,
        item: DashboardTransactionSection?
    ) {
        item?.let {
            holder?.onBind(it.dateHeader, it.amountDailyTransaction, currencySymbol)
        }
    }

    override fun onBindChildViewHolder(
        holder: DashboardTransactionItemViewHolder?,
        sectionPosition: Int,
        childPosition: Int,
        item: Transaction?
    ) {
        item?.let {
            holder?.onBind(it, currencySymbol)
        }

        holder?.itemView?.setOnClickListener {
            listener?.onClickTransaction(item)
        }
    }

    private fun clearSection() {
        sections.clear()
        currencySymbol = null
    }
}