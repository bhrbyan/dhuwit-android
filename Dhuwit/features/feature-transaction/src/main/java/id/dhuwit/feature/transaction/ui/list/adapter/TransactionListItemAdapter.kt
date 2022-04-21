package id.dhuwit.feature.transaction.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.feature.transaction.databinding.TransactionListItemBinding

class TransactionListItemAdapter(
    val transactions: MutableList<Transaction>,
    private val currencySymbol: String?,
    private val listener: TransactionListItemListener?
) : RecyclerView.Adapter<TransactionListItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionListItemViewHolder {
        return TransactionListItemViewHolder(
            TransactionListItemBinding.inflate(
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

    override fun onBindViewHolder(holder: TransactionListItemViewHolder, position: Int) {
        holder.onBind(transactions[position], currencySymbol)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}