package id.dhuwit.feature.account.ui.selection.adapter

import androidx.recyclerview.widget.DiffUtil
import id.dhuwit.core.account.model.Account

class AccountSelectionDiffUtil : DiffUtil.ItemCallback<Account>() {

    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }

}