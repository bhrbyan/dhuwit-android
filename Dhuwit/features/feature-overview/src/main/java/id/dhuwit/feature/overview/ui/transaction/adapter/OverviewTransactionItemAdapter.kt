package id.dhuwit.feature.overview.ui.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.feature.overview.databinding.OverviewTransactionItemBinding

class OverviewTransactionItemAdapter(
    val transactions: MutableList<Transaction>,
    private val currencySymbol: String?,
    private val listener: OverviewTransactionItemListener?
) : RecyclerView.Adapter<OverviewTransactionItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OverviewTransactionItemViewHolder {
        return OverviewTransactionItemViewHolder(
            OverviewTransactionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClickTransaction(transactions[adapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: OverviewTransactionItemViewHolder, position: Int) {
        holder.onBind(transactions[position], currencySymbol)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}