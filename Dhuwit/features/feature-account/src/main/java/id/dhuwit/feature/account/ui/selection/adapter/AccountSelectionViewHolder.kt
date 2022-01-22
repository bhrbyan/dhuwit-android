package id.dhuwit.feature.account.ui.selection.adapter

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.feature.account.databinding.AccountSelectionItemBinding

class AccountSelectionViewHolder(private val binding: AccountSelectionItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(account: Account) {
        binding.textName.text = account.name
    }

}