package id.dhuwit.feature.account.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.feature.account.databinding.AccountListAddBinding
import id.dhuwit.feature.account.databinding.AccountListItemBinding
import id.dhuwit.storage.Storage

class AccountListAdapter(private val storage: Storage) :
    ListAdapter<AccountListViewType, RecyclerView.ViewHolder>(
        AccountListDiffUtilCallback()
    ) {

    var listener: AccountListListener? = null

    fun updateAccounts(accounts: List<Account>?) {
        val list = when (accounts) {
            null -> {
                listOf(AccountListViewType.Add)
            }
            else -> {
                accounts.map { account -> AccountListViewType.Item(account) } + listOf(
                    AccountListViewType.Add
                )
            }
        }

        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> AccountListItemViewHolder(
                AccountListItemBinding.inflate(
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

            VIEW_TYPE_ADD -> AccountListAddViewHolder(
                AccountListAddBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            ).apply {
                itemView.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener?.onClickAddAccount()
                    }
                }
            }

            else -> throw ClassCastException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AccountListItemViewHolder -> {
                val account = getItem(position) as AccountListViewType.Item
                holder.onBind(account.account, storage)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AccountListViewType.Add -> VIEW_TYPE_ADD
            is AccountListViewType.Item -> VIEW_TYPE_ITEM
            else -> VIEW_TYPE_ITEM
        }
    }

    companion object {
        private const val VIEW_TYPE_ITEM: Int = 0
        const val VIEW_TYPE_ADD: Int = 1001
    }
}