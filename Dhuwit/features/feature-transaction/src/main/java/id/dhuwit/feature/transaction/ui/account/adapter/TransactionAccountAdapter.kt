package id.dhuwit.feature.transaction.ui.account.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.feature.transaction.databinding.TransactionAccountItemBinding

class TransactionAccountAdapter :
    ListAdapter<Account, TransactionAccountViewHolder>(TransactionAccountDiffUtil()) {

    var listener: TransactionAccountListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionAccountViewHolder {
        return TransactionAccountViewHolder(
            TransactionAccountItemBinding.inflate(
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

    override fun onBindViewHolder(holder: TransactionAccountViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}