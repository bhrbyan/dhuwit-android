package id.dhuwit.feature.account.ui.list

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.feature.account.databinding.AccountListItemBinding
import id.dhuwit.storage.Storage

class AccountListItemViewHolder(private val binding: AccountListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(account: Account, storage: Storage) {
        with(binding) {
            textAccountName.text = account.name
            textAccountBalance.text = account.balance.convertPriceWithCurrencyFormat(
                storage.getSymbolCurrency()
            )
        }
    }
}