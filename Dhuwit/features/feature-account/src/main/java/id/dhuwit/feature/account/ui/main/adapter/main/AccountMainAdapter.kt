package id.dhuwit.feature.account.ui.main.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.setting.user.SettingUser
import id.dhuwit.feature.account.databinding.AccountMainItemBinding

class AccountMainAdapter(private val settingUser: SettingUser) :
    ListAdapter<Account, AccountMainViewHolder>(object : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountMainViewHolder {
        return AccountMainViewHolder(
            AccountMainItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AccountMainViewHolder, position: Int) {
        holder.onBind(getItem(position), settingUser)
    }
}