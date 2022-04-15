package id.dhuwit.feature.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.transaction.model.Transaction
import id.dhuwit.feature.dashboard.databinding.DashboardTransactionItemBinding

class DashboardTransactionItemAdapter(
    val transactions: MutableList<Transaction>,
    private val currencySymbol: String?,
    private val listener: DashboardTransactionItemListener?
) : RecyclerView.Adapter<DashboardTransactionItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DashboardTransactionItemViewHolder {
        return DashboardTransactionItemViewHolder(
            DashboardTransactionItemBinding.inflate(
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

    override fun onBindViewHolder(holder: DashboardTransactionItemViewHolder, position: Int) {
        holder.onBind(transactions[position], currencySymbol)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}