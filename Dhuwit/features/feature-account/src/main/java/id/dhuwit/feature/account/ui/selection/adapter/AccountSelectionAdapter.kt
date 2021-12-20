package id.dhuwit.feature.account.ui.selection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.feature.account.databinding.AccountSelectionItemBinding

class AccountSelectionAdapter :
    ListAdapter<Account, AccountSelectionViewHolder>(AccountSelectionDiffUtil()) {

    var listener: AccountSelectionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountSelectionViewHolder {
        return AccountSelectionViewHolder(
            AccountSelectionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onClickAccount(getItem(adapterPosition).id)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: AccountSelectionViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}