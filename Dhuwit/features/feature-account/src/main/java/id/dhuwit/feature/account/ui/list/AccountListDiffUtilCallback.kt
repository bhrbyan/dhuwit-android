package id.dhuwit.feature.account.ui.list

import androidx.recyclerview.widget.DiffUtil

class AccountListDiffUtilCallback : DiffUtil.ItemCallback<AccountListViewType>() {
    override fun areItemsTheSame(
        oldItem: AccountListViewType,
        newItem: AccountListViewType
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: AccountListViewType,
        newItem: AccountListViewType
    ): Boolean {
        return oldItem == newItem
    }
}