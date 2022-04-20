package id.dhuwit.feature.overview.ui.account.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.transaction.model.TransactionAccount
import id.dhuwit.feature.overview.databinding.OverviewAccountItemBinding

class OverviewAccountAdapter(private val currencySymbol: String?) :
    RecyclerView.Adapter<OverviewAccountViewHolder>() {

    private var accounts: List<TransactionAccount> = emptyList()
    var listener: OverviewAccountListener? = null

    fun updateList(accounts: List<TransactionAccount>) {
        this.accounts = accounts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewAccountViewHolder {
        return OverviewAccountViewHolder(
            OverviewAccountItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClickAccount(accounts[adapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: OverviewAccountViewHolder, position: Int) {
        holder.onBind(accounts[position], currencySymbol)
    }

    override fun getItemCount(): Int {
        return accounts.size
    }
}