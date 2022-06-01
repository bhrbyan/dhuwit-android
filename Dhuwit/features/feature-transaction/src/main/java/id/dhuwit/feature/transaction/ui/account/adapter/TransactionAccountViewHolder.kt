package id.dhuwit.feature.transaction.ui.account.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.feature.transaction.databinding.TransactionAccountItemBinding

class TransactionAccountViewHolder(private val binding: TransactionAccountItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(account: Account) {
        binding.textName.text = account.name
    }

}