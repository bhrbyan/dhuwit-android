package id.dhuwit.feature.account.ui.main.adapter.main

import androidx.recyclerview.widget.RecyclerView
import id.dhuwit.core.account.model.Account
import id.dhuwit.core.extension.convertPriceWithCurrencyFormat
import id.dhuwit.core.extension.gone
import id.dhuwit.core.extension.visible
import id.dhuwit.feature.account.R
import id.dhuwit.feature.account.databinding.AccountMainItemBinding
import id.dhuwit.storage.Storage

class AccountMainViewHolder(private val binding: AccountMainItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(account: Account, storage: Storage) {
        binding.textAccountName.text =
            binding.root.context.getString(R.string.account_main_account_name, account.name)
        binding.textAccountBalance.text =
            account.balance.convertPriceWithCurrencyFormat(storage.getSymbolCurrency())

        if (account.isPrimary) {
            binding.imagePrimaryAccount?.visible()
        } else {
            binding.imagePrimaryAccount?.gone()
        }
    }

}